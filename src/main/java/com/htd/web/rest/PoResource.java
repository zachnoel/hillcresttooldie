package com.htd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.htd.domain.Po;
import com.htd.repository.PoRepository;
import com.htd.web.rest.util.PaginationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

//File upload imports
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;






//Apache POI imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * REST controller for managing Po.
 */
@RestController
@RequestMapping("/api")
public class PoResource {

    private final Logger log = LoggerFactory.getLogger(PoResource.class);

    @Inject
    private PoRepository poRepository;

    
    /**
     * Upload PO file for processing.
     */
    //@RequestMapping(value = "/fileupload/po", method = RequestMethod.POST)
    //public @ResponseBody String postTest(@RequestRaram("file")){
    //log.debug("//////////////////////////////File Uploadeding ///////////////////////////");
    //}
    @RequestMapping(value="/fileupload/po", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        log.debug("//////////////////////////////File Uploadeding ///////////////////////////");
 /*       
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                log.debug("bytes = " + bytes);
                //log.debug(stream);
                stream.close();
                

                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
*/  

        try {
             
            FileInputStream file2 = new FileInputStream(new File("C:\\Users\\noelz\\Desktop\\hillcrestToolDie\\hillcrestToolDie\\docs\\material.xlsx"));
             
            //Get the workbook instance for XLS file 
            XSSFWorkbook workbook = new XSSFWorkbook(file2);
 
            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
             
            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                 
                //For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext()) {
                     
                    Cell cell = cellIterator.next();
                     
                    switch(cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "\t\t");
                            break;
                    }
                }
                System.out.println("");
            }
            file2.close();
           // FileOutputStream out = new FileOutputStream(new File("C:\\test.xlsx"));
           // workbook.write(out);
           // out.close();
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
