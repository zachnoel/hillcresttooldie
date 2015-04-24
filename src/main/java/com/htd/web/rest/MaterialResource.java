package com.htd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.htd.domain.Material;
import com.htd.repository.MaterialRepository;
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
 * REST controller for managing Material.
 */
@RestController
@RequestMapping("/api")
public class MaterialResource {

    private final Logger log = LoggerFactory.getLogger(MaterialResource.class);

    @Inject
    private MaterialRepository materialRepository;

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
