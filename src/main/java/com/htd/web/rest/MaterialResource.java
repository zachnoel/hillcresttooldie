package com.htd.web.rest;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.htd.domain.Material;
import com.htd.repository.MaterialRepository;
import com.htd.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Material.
 */
@RestController
@RequestMapping("/api")
public class MaterialResource {

    private final Logger log = LoggerFactory.getLogger(MaterialResource.class);
    private Map<Long,  Material> materialMap = new HashMap<Long,  Material>();
    private String material_number, material_size;
    private BigDecimal material_thickness, lb_per_sheet, dollar_per_lb;
    
    @Inject
    private MaterialRepository materialRepository;

    @RequestMapping(value="/fileupload/material", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file){
                    
        try {
        	
            byte[] byteArr = file.getBytes();
        	
            InputStream file2 = new ByteArrayInputStream(byteArr);
             
            //Get the workbook instance for XLS file 
            XSSFWorkbook workbook = new XSSFWorkbook(file2);
 
            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {

				Row row = rit.next();
				Material material = new Material();
				
				if(row.getRowNum()==0){
					continue;
				}

				Iterator<Cell> cit = row.cellIterator();
				Cell cell;
				
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					this.material_number = cell.getStringCellValue();
					material.setMaterial_number(material_number);
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String materialThicknessAsString = cell.getStringCellValue();
					double materalThicknessAsDouble = Double.parseDouble(materialThicknessAsString);
					//now the conversion into big decimal happens
					BigDecimal materialThickness = new BigDecimal(materalThicknessAsDouble,MathContext.DECIMAL64);
					this.material_thickness = materialThickness;
					material.setMaterial_thickness(materialThickness);
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					this.material_size = cell.getStringCellValue();
					material.setMaterial_size(material_size);
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String lb_per_sheet_as_string = cell.getStringCellValue();
					double lbs_per_sheet_asDouble = Double.parseDouble(lb_per_sheet_as_string);
					//now the conversion into big decimal happens
					BigDecimal lbs_per_sheet_as_BigDecimal = new BigDecimal(lbs_per_sheet_asDouble,MathContext.DECIMAL64);
					this.lb_per_sheet = lbs_per_sheet_as_BigDecimal;
					material.setLb_per_sheet(lbs_per_sheet_as_BigDecimal);
				}
				if (cit.hasNext()) {
					cell = cit.next();
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String dollar_per_lb = cell.getStringCellValue();
					Double perLb_as_double = Double.parseDouble(dollar_per_lb);
					//now the conversion into big decimal happens
					BigDecimal dollar_per_lb_as_BigDecimal = new BigDecimal(perLb_as_double,MathContext.DECIMAL64);
					this.dollar_per_lb = dollar_per_lb_as_BigDecimal;
					material.setDollar_per_lb(dollar_per_lb_as_BigDecimal);
				}

				Long materialValue = Long.parseLong(material.getMaterial_number());

				//puts materials into map and the key is the material number
				materialMap.put(materialValue, material);
				
				materialRepository.save(material);
			}
			
			workbook.close();

             
         //test to make sure the file is outputting
		for(Map.Entry<Long,  Material> entry : materialMap.entrySet()){
			 Material it = (Material) entry.getValue();
			
			System.out.println("Key: "+entry.getKey() + " " +"Thinkness "+it.getMaterial_thickness()+" Size: "+ it.getMaterial_size() + " "
					+ it.getLb_per_sheet() +" Dollar per lbs: "+it.getDollar_per_lb());

		}
		
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "processing done of file";

    }
    
    /**
     * POST  /materials -> Create a new material.
     */
    @RequestMapping(value = "/materials",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Material material) throws URISyntaxException {
        log.debug("REST request to save Material : {}", material);
        if (material.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new material cannot already have an ID").build();
        }
        materialRepository.save(material);
        return ResponseEntity.created(new URI("/api/materials/" + material.getId())).build();
    }

    /**
     * PUT  /materials -> Updates an existing material.
     */
    @RequestMapping(value = "/materials",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Material material) throws URISyntaxException {
        log.debug("REST request to update Material : {}", material);
        if (material.getId() == null) {
            return create(material);
        }
        materialRepository.save(material);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /materials -> get all the materials.
     */
    @RequestMapping(value = "/materials",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Material>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Material> page = materialRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/materials", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /materials/:id -> get the "id" material.
     */
    @RequestMapping(value = "/materials/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Material> get(@PathVariable Long id) {
        log.debug("REST request to get Material : {}", id);
        return Optional.ofNullable(materialRepository.findOne(id))
            .map(material -> new ResponseEntity<>(
                material,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /materials/:id -> delete the "id" material.
     */
    @RequestMapping(value = "/materials/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Material : {}", id);
        materialRepository.delete(id);
    }
}
