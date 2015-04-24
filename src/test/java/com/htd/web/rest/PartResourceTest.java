package com.htd.web.rest;

import com.htd.Application;
import com.htd.domain.Part;
import com.htd.repository.PartRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PartResource REST controller.
 *
 * @see PartResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PartResourceTest {

    private static final String DEFAULT_PART_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_PART_NUMBER = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_PLASMA_HRS_PER_PART = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_PLASMA_HRS_PER_PART = BigDecimal.ONE;

    private static final BigDecimal DEFAULT_GRIND_HRS_PER_PART = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_GRIND_HRS_PER_PART = BigDecimal.ONE;

    private static final BigDecimal DEFAULT_MILL_HRS_PER_PART = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_MILL_HRS_PER_PART = BigDecimal.ONE;

    private static final BigDecimal DEFAULT_BRAKEPRESS_HRS_PER_PART = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_BRAKEPRESS_HRS_PER_PART = BigDecimal.ONE;

    private static final BigDecimal DEFAULT_LB_PER_PART = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_LB_PER_PART = BigDecimal.ONE;

    private static final Integer DEFAULT_INVENTORY_COUNT = 0;
    private static final Integer UPDATED_INVENTORY_COUNT = 1;

    @Inject
    private PartRepository partRepository;

    private MockMvc restPartMockMvc;

    private Part part;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PartResource partResource = new PartResource();
        ReflectionTestUtils.setField(partResource, "partRepository", partRepository);
        this.restPartMockMvc = MockMvcBuilders.standaloneSetup(partResource).build();
    }

    @Before
    public void initTest() {
        part = new Part();
        part.setPart_number(DEFAULT_PART_NUMBER);
        part.setPlasma_hrs_per_part(DEFAULT_PLASMA_HRS_PER_PART);
        part.setGrind_hrs_per_part(DEFAULT_GRIND_HRS_PER_PART);
        part.setMill_hrs_per_part(DEFAULT_MILL_HRS_PER_PART);
        part.setBrakepress_hrs_per_part(DEFAULT_BRAKEPRESS_HRS_PER_PART);
        part.setLb_per_part(DEFAULT_LB_PER_PART);
        part.setInventory_count(DEFAULT_INVENTORY_COUNT);
    }

    @Test
    @Transactional
    public void createPart() throws Exception {
        int databaseSizeBeforeCreate = partRepository.findAll().size();

        // Create the Part
        restPartMockMvc.perform(post("/api/parts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(part)))
                .andExpect(status().isCreated());

        // Validate the Part in the database
        List<Part> parts = partRepository.findAll();
        assertThat(parts).hasSize(databaseSizeBeforeCreate + 1);
        Part testPart = parts.get(parts.size() - 1);
        assertThat(testPart.getPart_number()).isEqualTo(DEFAULT_PART_NUMBER);
        assertThat(testPart.getPlasma_hrs_per_part()).isEqualTo(DEFAULT_PLASMA_HRS_PER_PART);
        assertThat(testPart.getGrind_hrs_per_part()).isEqualTo(DEFAULT_GRIND_HRS_PER_PART);
        assertThat(testPart.getMill_hrs_per_part()).isEqualTo(DEFAULT_MILL_HRS_PER_PART);
        assertThat(testPart.getBrakepress_hrs_per_part()).isEqualTo(DEFAULT_BRAKEPRESS_HRS_PER_PART);
        assertThat(testPart.getLb_per_part()).isEqualTo(DEFAULT_LB_PER_PART);
        assertThat(testPart.getInventory_count()).isEqualTo(DEFAULT_INVENTORY_COUNT);
    }

    @Test
    @Transactional
    public void getAllParts() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the parts
        restPartMockMvc.perform(get("/api/parts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(part.getId().intValue())))
                .andExpect(jsonPath("$.[*].part_number").value(hasItem(DEFAULT_PART_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].plasma_hrs_per_part").value(hasItem(DEFAULT_PLASMA_HRS_PER_PART.intValue())))
                .andExpect(jsonPath("$.[*].grind_hrs_per_part").value(hasItem(DEFAULT_GRIND_HRS_PER_PART.intValue())))
                .andExpect(jsonPath("$.[*].mill_hrs_per_part").value(hasItem(DEFAULT_MILL_HRS_PER_PART.intValue())))
                .andExpect(jsonPath("$.[*].brakepress_hrs_per_part").value(hasItem(DEFAULT_BRAKEPRESS_HRS_PER_PART.intValue())))
                .andExpect(jsonPath("$.[*].lb_per_part").value(hasItem(DEFAULT_LB_PER_PART.intValue())))
                .andExpect(jsonPath("$.[*].inventory_count").value(hasItem(DEFAULT_INVENTORY_COUNT)));
    }

    @Test
    @Transactional
    public void getPart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get the part
        restPartMockMvc.perform(get("/api/parts/{id}", part.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(part.getId().intValue()))
            .andExpect(jsonPath("$.part_number").value(DEFAULT_PART_NUMBER.toString()))
            .andExpect(jsonPath("$.plasma_hrs_per_part").value(DEFAULT_PLASMA_HRS_PER_PART.intValue()))
            .andExpect(jsonPath("$.grind_hrs_per_part").value(DEFAULT_GRIND_HRS_PER_PART.intValue()))
            .andExpect(jsonPath("$.mill_hrs_per_part").value(DEFAULT_MILL_HRS_PER_PART.intValue()))
            .andExpect(jsonPath("$.brakepress_hrs_per_part").value(DEFAULT_BRAKEPRESS_HRS_PER_PART.intValue()))
            .andExpect(jsonPath("$.lb_per_part").value(DEFAULT_LB_PER_PART.intValue()))
            .andExpect(jsonPath("$.inventory_count").value(DEFAULT_INVENTORY_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingPart() throws Exception {
        // Get the part
        restPartMockMvc.perform(get("/api/parts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);
		
		int databaseSizeBeforeUpdate = partRepository.findAll().size();

        // Update the part
        part.setPart_number(UPDATED_PART_NUMBER);
        part.setPlasma_hrs_per_part(UPDATED_PLASMA_HRS_PER_PART);
        part.setGrind_hrs_per_part(UPDATED_GRIND_HRS_PER_PART);
        part.setMill_hrs_per_part(UPDATED_MILL_HRS_PER_PART);
        part.setBrakepress_hrs_per_part(UPDATED_BRAKEPRESS_HRS_PER_PART);
        part.setLb_per_part(UPDATED_LB_PER_PART);
        part.setInventory_count(UPDATED_INVENTORY_COUNT);
        restPartMockMvc.perform(put("/api/parts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(part)))
                .andExpect(status().isOk());

        // Validate the Part in the database
        List<Part> parts = partRepository.findAll();
        assertThat(parts).hasSize(databaseSizeBeforeUpdate);
        Part testPart = parts.get(parts.size() - 1);
        assertThat(testPart.getPart_number()).isEqualTo(UPDATED_PART_NUMBER);
        assertThat(testPart.getPlasma_hrs_per_part()).isEqualTo(UPDATED_PLASMA_HRS_PER_PART);
        assertThat(testPart.getGrind_hrs_per_part()).isEqualTo(UPDATED_GRIND_HRS_PER_PART);
        assertThat(testPart.getMill_hrs_per_part()).isEqualTo(UPDATED_MILL_HRS_PER_PART);
        assertThat(testPart.getBrakepress_hrs_per_part()).isEqualTo(UPDATED_BRAKEPRESS_HRS_PER_PART);
        assertThat(testPart.getLb_per_part()).isEqualTo(UPDATED_LB_PER_PART);
        assertThat(testPart.getInventory_count()).isEqualTo(UPDATED_INVENTORY_COUNT);
    }

    @Test
    @Transactional
    public void deletePart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);
		
		int databaseSizeBeforeDelete = partRepository.findAll().size();

        // Get the part
        restPartMockMvc.perform(delete("/api/parts/{id}", part.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Part> parts = partRepository.findAll();
        assertThat(parts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
