package com.htd.web.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.hillcresttooldie.model.Items;
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

    @Inject
    private PoRepository poRepository;

    
    /**
     * Upload PO file for processing.
     */  
    @RequestMapping(value="/fileupload/po", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        
       
       /* String mat = null, thick=null, size=null, lbsPerSheet=null, lbs=null;
        try {
        	
            byte[] byteArr = file.getBytes();
        	
            InputStream file2 = new ByteArrayInputStream(byteArr);
             
            //Get the workbook instance for XLS file 
            XSSFWorkbook workbook = new XSSFWorkbook(file2);
 
            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {

				Items items = new Items();
				Row row = rit.next();
				
				if(row.getRowNum()==0){
					continue;
				}

				Iterator<Cell> cit = row.cellIterator();
				Cell cell;

				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					mat = cell.getStringCellValue();
					

				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					thick = cell.getStringCellValue();
					
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					size = cell.getStringCellValue();
					
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					lbsPerSheet = cell.getStringCellValue();
					
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					lbs = cell.getStringCellValue();
					
				}

				

				//puts items into map and the key is the job number
				
			}
			
			workbook.close();
		
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
