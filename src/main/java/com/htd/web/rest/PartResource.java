package com.htd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.htd.domain.Part;
import com.htd.repository.PartRepository;
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

/**
 * REST controller for managing Part.
 */
@RestController
@RequestMapping("/api")
public class PartResource {

    private final Logger log = LoggerFactory.getLogger(PartResource.class);

    @Inject
    private PartRepository partRepository;

    /**
     * POST  /parts -> Create a new part.
     */
    @RequestMapping(value = "/parts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Part part) throws URISyntaxException {
        log.debug("REST request to save Part : {}", part);
        if (part.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new part cannot already have an ID").build();
        }
        partRepository.save(part);
        return ResponseEntity.created(new URI("/api/parts/" + part.getId())).build();
    }

    /**
     * PUT  /parts -> Updates an existing part.
     */
    @RequestMapping(value = "/parts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Part part) throws URISyntaxException {
        log.debug("REST request to update Part : {}", part);
        if (part.getId() == null) {
            return create(part);
        }
        partRepository.save(part);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /parts -> get all the parts.
     */
    @RequestMapping(value = "/parts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Part>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Part> page = partRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/parts", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /parts/:id -> get the "id" part.
     */
    @RequestMapping(value = "/parts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Part> get(@PathVariable Long id) {
        log.debug("REST request to get Part : {}", id);
        return Optional.ofNullable(partRepository.findOneWithEagerRelationships(id))
            .map(part -> new ResponseEntity<>(
                part,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parts/:id -> delete the "id" part.
     */
    @RequestMapping(value = "/parts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Part : {}", id);
        partRepository.delete(id);
    }
}
