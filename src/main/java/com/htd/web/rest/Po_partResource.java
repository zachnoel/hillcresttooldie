package com.htd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.htd.domain.Po_part;
import com.htd.repository.Po_partRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Po_part.
 */
@RestController
@RequestMapping("/api")
public class Po_partResource {

    private final Logger log = LoggerFactory.getLogger(Po_partResource.class);

    @Inject
    private Po_partRepository po_partRepository;

    /**
     * POST  /po_parts -> Create a new po_part.
     */
    @RequestMapping(value = "/po_parts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Po_part po_part) throws URISyntaxException {
        log.debug("REST request to save Po_part : {}", po_part);
        if (po_part.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new po_part cannot already have an ID").build();
        }
        po_partRepository.save(po_part);
        return ResponseEntity.created(new URI("/api/po_parts/" + po_part.getId())).build();
    }

    /**
     * PUT  /po_parts -> Updates an existing po_part.
     */
    @RequestMapping(value = "/po_parts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Po_part po_part) throws URISyntaxException {
        log.debug("REST request to update Po_part : {}", po_part);
        if (po_part.getId() == null) {
            return create(po_part);
        }
        po_partRepository.save(po_part);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /po_parts -> get all the po_parts.
     */
    @RequestMapping(value = "/po_parts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Po_part> getAll() {
        log.debug("REST request to get all Po_parts");
        return po_partRepository.findAll();
    }
    
    /**
     * GET  /po_parts_by_po -> get all the po_parts for a given po ID.
     */
    @RequestMapping(value = "/po_parts_by_po/{poId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Po_part> getPartsByPo(@PathVariable Long poId) {
    	log.debug("REST request to get all Po_parts by a given PO ID. PO ID given = " + poId);
        return po_partRepository.findByPoId(poId);
    }

    /**
     * GET  /po_parts/:id -> get the "id" po_part.
     */
    @RequestMapping(value = "/po_parts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Po_part> get(@PathVariable Long id) {
        log.debug("REST request to get Po_part : {}", id);
        return Optional.ofNullable(po_partRepository.findOne(id))
            .map(po_part -> new ResponseEntity<>(
                po_part,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    

    /**
     * DELETE  /po_parts/:id -> delete the "id" po_part.
     */
    @RequestMapping(value = "/po_parts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Po_part : {}", id);
        po_partRepository.delete(id);
    }
}
