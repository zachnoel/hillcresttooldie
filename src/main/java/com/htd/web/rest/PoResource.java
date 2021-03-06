package com.htd.web.rest;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.htd.web.rest.util.JobOrderGenerator;
import com.htd.domain.Po;
import com.htd.domain.ShopOrder;
import com.htd.repository.MaterialRepository;
import com.htd.repository.PoRepository;
import com.htd.web.rest.util.PaginationUtil;

//File upload imports
//Apache POI imports
/**
 * REST controller for managing Po.
 */
@RestController
@RequestMapping("/api")
public class PoResource {

    private final Logger log = LoggerFactory.getLogger(PoResource.class);
    private Map<Integer,Po> poMap = new HashMap<Integer,Po>();

    @Inject
    private PoRepository poRepository;

    @Inject
    MaterialRepository materialRepository;

    @Inject
    private Environment env;

    private JobOrderGenerator jobOrderGenerator;

    /*
     * Upload PO file for processing.
     */
    @RequestMapping(value="/fileupload/po", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file){

    	/*if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The 'name' parameter must not be null or empty");
        }*/


        try {
            byte[] byteArr = file.getBytes();

            InputStream file2 = new ByteArrayInputStream(byteArr);

            //Get the workbook instance for XLS file
            XSSFWorkbook workbook = new XSSFWorkbook(file2);

            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {

                Row row = rit.next();
                Po po = new Po();
                //skip header in excel file
                if(row.getRowNum()==0){
                    continue;
                }
                Iterator<Cell> cit = row.cellIterator();
                Cell cell;

                if (cit.hasNext()) {
                    cell = cit.next();
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    Long _id = Long.parseLong(cell.getStringCellValue());
                    po.setId(_id);
                }
                if (cit.hasNext()) {
                    cell = cit.next();
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String _poNumber = cell.getStringCellValue();
                    po.setPo_number(_poNumber);

                }
                if (cit.hasNext()) {
                    cell = cit.next();
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String _salesOrder = cell.getStringCellValue();

                    po.setSales_order_number(_salesOrder);
                }
                if (cit.hasNext()) {
                    cell = cit.next();
                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);

                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String cellValue = sdf.format(cell.getDateCellValue());

                        //System.out.println(cellValue);

                        //bufferDate is getting mm/dd/yyyy from excel cell
                        String[] dateSpliter = cellValue.split("/");
                        int month= Integer.parseInt(dateSpliter[0]);
                        int day= Integer.parseInt(dateSpliter[1]);
                        int year= Integer.parseInt(dateSpliter[2]);

                        LocalDate _date = new LocalDate(year,month,day);

                        po.setDue_date(_date);
                    }
                }
                if (cit.hasNext()) {
                    cell = cit.next();
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String _status = cell.getStringCellValue();

                    po.setStatus(_status);
                }
                if (cit.hasNext()) {
                    cell = cit.next();
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    Double _totalSale = Double.parseDouble(cell.getStringCellValue());
                    //converting double to BigDecimal
                    BigDecimal totalSale_bigDecimal = new BigDecimal(_totalSale,MathContext.DECIMAL64);
                    po.setTotal_sale(totalSale_bigDecimal);

                }

                int poNumber = Integer.parseInt(po.getPo_number());
                //puts items into map and the key is the PO number
                poMap.put(poNumber, po);

                poRepository.save(po);

            }
            //test to make sure the file is outputting
            for(Map.Entry<Integer,  Po> entry : poMap.entrySet()){
                Po it = ( Po) entry.getValue();

                System.out.println("Key: "+entry.getKey() + " " +"PO Number "+it.getPo_number()+" "+ "Sales Number "+it.getSales_order_number() + " "+
                    " Date "+ it.getDue_date()+" "+it.getStatus()+" Total Sale: "+it.getTotal_sale());
            }
            workbook.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Array out of bounds "+e);
        }
        return "processing done of file";

    }
    /**
     * POST  /pos -> Create a new po.
     */
    @RequestMapping(value = "/pos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Po po) throws URISyntaxException {
        log.debug("REST request to save Po : {}", po);
        if (po.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new po cannot already have an ID").build();
        }
        poRepository.save(po);
        return ResponseEntity.created(new URI("/api/pos/" + po.getId())).build();
    }

    /**
     * PUT  /pos -> Updates an existing po.
     */
    @RequestMapping(value = "/pos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Po po) throws URISyntaxException {
        log.debug("REST request to update Po : {}", po);
        if (po.getId() == null) {
            return create(po);
        }
        poRepository.save(po);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /pos -> get all the pos.
     */
    @RequestMapping(value = "/pos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Po>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                           @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Po> page = poRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pos", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /pos -> get all the pos with start and end date filters.
     * @throws ParseException
     */
    @RequestMapping(value = "/filteredPos/{startDate}/{endDate}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Po>> getFilteredPos(@PathVariable String startDate, @PathVariable String endDate) throws ParseException{

        log.debug("REST request to get pos between start and end dates");
        List<Po> poResults = poRepository.findByFilter(startDate, endDate);
        return new ResponseEntity<>(poResults, HttpStatus.OK);
    }

    /**
     * GET  /pos/:id -> get the "id" po.
     */
    /*@RequestMapping(value = "/pos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Po> get(@PathVariable Long id) {
        log.debug("REST request to get Po : {}", id);
        return Optional.ofNullable(poRepository.findOneWithEagerRelationships(id))
            .map(po -> new ResponseEntity<>(
                po,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/
    @RequestMapping(value = "/pos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Po> get(@PathVariable Long id) {
        log.debug("REST request to get Po : {}", id);
        return Optional.ofNullable(poRepository.findOne(id))
            .map(po -> new ResponseEntity<>(
                po,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pos/:id -> delete the "id" po.
     */
    @RequestMapping(value = "/pos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Po : {}", id);
        poRepository.delete(id);
    }

    /*
     * Generate Shop Orders.
     */
    @RequestMapping(value = "/generateJobTicket/{id}", method = RequestMethod.GET)
    public void generateJobTicket(@PathVariable Long id, HttpServletResponse response) throws URISyntaxException {

        List<ShopOrder> shopOrder  = poRepository.getShopOrder(id);

        for(ShopOrder s: shopOrder){
            System.out.println("ShopOrder: "+s); //nothing is displayed
        }

        try {
            System.out.println("Trying to make jobOrder");  //this runs
            jobOrderGenerator = new JobOrderGenerator(shopOrder,response);
            System.out.println("Processed job order"); //this too

        } catch (IOException ex) {
            System.out.print("Was not able to create job orders");


        } catch (InvalidFormatException e) {

            System.out.print("Invalid format, unable to create job order");

        }

    }

}
