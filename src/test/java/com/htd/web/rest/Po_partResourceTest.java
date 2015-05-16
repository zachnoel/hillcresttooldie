package com.htd.web.rest;

import com.htd.Application;
import com.htd.domain.Po_part;
import com.htd.repository.Po_partRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the Po_partResource REST controller.
 *
 * @see Po_partResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Po_partResourceTest {


    private static final Integer DEFAULT_PART_QUANTITY = 0;
    private static final Integer UPDATED_PART_QUANTITY = 1;

    @Inject
    private Po_partRepository po_partRepository;

    private MockMvc restPo_partMockMvc;

    private Po_part po_part;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Po_partResource po_partResource = new Po_partResource();
        ReflectionTestUtils.setField(po_partResource, "po_partRepository", po_partRepository);
        this.restPo_partMockMvc = MockMvcBuilders.standaloneSetup(po_partResource).build();
    }

    @Before
    public void initTest() {
        po_part = new Po_part();
        po_part.setPart_quantity(DEFAULT_PART_QUANTITY);
    }

    @Test
    @Transactional
    public void createPo_part() throws Exception {
        int databaseSizeBeforeCreate = po_partRepository.findAll().size();

        // Create the Po_part
        restPo_partMockMvc.perform(post("/api/po_parts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(po_part)))
                .andExpect(status().isCreated());

        // Validate the Po_part in the database
        List<Po_part> po_parts = po_partRepository.findAll();
        assertThat(po_parts).hasSize(databaseSizeBeforeCreate + 1);
        Po_part testPo_part = po_parts.get(po_parts.size() - 1);
        assertThat(testPo_part.getPart_quantity()).isEqualTo(DEFAULT_PART_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllPo_parts() throws Exception {
        // Initialize the database
        po_partRepository.saveAndFlush(po_part);

        // Get all the po_parts
        restPo_partMockMvc.perform(get("/api/po_parts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(po_part.getId().intValue())))
                .andExpect(jsonPath("$.[*].part_quantity").value(hasItem(DEFAULT_PART_QUANTITY)));
    }

    @Test
    @Transactional
    public void getPo_part() throws Exception {
        // Initialize the database
        po_partRepository.saveAndFlush(po_part);

        // Get the po_part
        restPo_partMockMvc.perform(get("/api/po_parts/{id}", po_part.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(po_part.getId().intValue()))
            .andExpect(jsonPath("$.part_quantity").value(DEFAULT_PART_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingPo_part() throws Exception {
        // Get the po_part
        restPo_partMockMvc.perform(get("/api/po_parts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePo_part() throws Exception {
        // Initialize the database
        po_partRepository.saveAndFlush(po_part);
		
		int databaseSizeBeforeUpdate = po_partRepository.findAll().size();

        // Update the po_part
        po_part.setPart_quantity(UPDATED_PART_QUANTITY);
        restPo_partMockMvc.perform(put("/api/po_parts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(po_part)))
                .andExpect(status().isOk());

        // Validate the Po_part in the database
        List<Po_part> po_parts = po_partRepository.findAll();
        assertThat(po_parts).hasSize(databaseSizeBeforeUpdate);
        Po_part testPo_part = po_parts.get(po_parts.size() - 1);
        assertThat(testPo_part.getPart_quantity()).isEqualTo(UPDATED_PART_QUANTITY);
    }

    @Test
    @Transactional
    public void deletePo_part() throws Exception {
        // Initialize the database
        po_partRepository.saveAndFlush(po_part);
		
		int databaseSizeBeforeDelete = po_partRepository.findAll().size();

        // Get the po_part
        restPo_partMockMvc.perform(delete("/api/po_parts/{id}", po_part.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Po_part> po_parts = po_partRepository.findAll();
        assertThat(po_parts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
