package com.htd.web.rest;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.hillcresttooldie.model.POModel;
import com.htd.domain.Po;
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
    private Map<Integer,POModel> poMap = new HashMap<Integer,POModel>();
    @Inject
    private PoRepository poRepository;

    
    /**
     * Upload PO file for processing.
     */  
    @RequestMapping(value="/fileupload/po", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        
       
    	int _id, _poNumber = 0;
    	String _salesOrder, _status;
    	DateTime _date;
    	double _totalSale;
   
    	
        try {
        	
            byte[] byteArr = file.getBytes();
        	
            InputStream file2 = new ByteArrayInputStream(byteArr);
             
            //Get the workbook instance for XLS file 
            XSSFWorkbook workbook = new XSSFWorkbook(file2);
 
            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {

				POModel po = new POModel();
				Row row = rit.next();
				
				//skip header in excel file
				if(row.getRowNum()==0){
					continue;
				}

				Iterator<Cell> cit = row.cellIterator();
				Cell cell;

				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					_id = Integer.parseInt(cell.getStringCellValue());
					po.setId(_id);
					

				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					_poNumber = Integer.parseInt(cell.getStringCellValue());
					po.setPoNumber(_poNumber);
					
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					_salesOrder = cell.getStringCellValue();
					
					po.setSalesOrder(_salesOrder);
					
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					//String bufferDate = cell.getStringCellValue();
					
					/*DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
					java.util.Date d =  cell.getDateCellValue();
					String buy_date = df.format(d);
					System.out.println("date is :- "+ buy_date);*/
					
					
					//for some reason the date is return a string, print it out and it is in weird
					//format. I am trying to figure out how to convert it to a date then store it
					// as a date using Joda. The issue is apache poi, not sure what the issue is at the moment.
					
					switch (cell.getCellType()) {
	                case Cell.CELL_TYPE_STRING:
	                    System.out.println("String "+cell.getRichStringCellValue().getString());
	                    break;
	                case Cell.CELL_TYPE_NUMERIC:
	                    if (DateUtil.isCellDateFormatted(cell)) {
	                        System.out.println("Date format "+cell.getDateCellValue());
	                    } else {
	                        System.out.println("not"+cell.getNumericCellValue());
	                    }
	                    break;
	                case Cell.CELL_TYPE_BOOLEAN:
	                    System.out.println("Bool "+cell.getBooleanCellValue());
	                    break;
	                case Cell.CELL_TYPE_FORMULA:
	                    System.out.println("Formula "+cell.getCellFormula());
	                    break;
	                default:
	                    System.out.println();
	            }
					
					
					/*
					if (DateUtil.isCellDateFormatted(cell))
					{
					   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					   String cellValue = sdf.format(cell.getDateCellValue());
					
					System.out.println(cellValue);
					
					//bufferDate is getting mm/dd/yyyy from excel cell
					String[] dateSpliter = bufferDate.split("/");
					String month= dateSpliter[0];
					String day= dateSpliter[1];
					String year= dateSpliter[2];
					
					int monthInt = Integer.parseInt(month);
					int dayInt = Integer.parseInt(day);
					int yearInt = Integer.parseInt(year);
					
					//joda DateTime
					_date = new DateTime(yearInt,monthInt,dayInt,0,0);
					
					po.setDate(_date);
					
					}*/
		
					
					
					
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					_status = cell.getStringCellValue();
					
					po.setStatus(_status);
					
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					_totalSale = Double.parseDouble(cell.getStringCellValue());
					
					po.setTotalSale(_totalSale);
					
				}
				//puts items into map and the key is the PO number
				poMap.put(_poNumber, po);
				
			}
            
            //test to make sure the file is outputting
    		for(Map.Entry<Integer,  POModel> entry : poMap.entrySet()){
    			POModel it = ( POModel) entry.getValue();
    			
    			System.out.println("Key: "+entry.getKey() + " " +"PO Number "+it.getPoNumber()+" "+ "Sales Number "+it.getSalesOrder() + " "+
    					" Date "+ it.getDate()+" "+it.getStatus()+" "+it.getTotalSale());

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
		return "processing done of file" + name;

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
     * GET  /pos/:id -> get the "id" po.
     */
    @RequestMapping(value = "/pos/{id}",
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
}
