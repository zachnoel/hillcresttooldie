package com.htd.web.rest;

import com.htd.Application;
import com.htd.domain.Material;
import com.htd.repository.MaterialRepository;

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
 * Test class for the MaterialResource REST controller.
 *
 * @see MaterialResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MaterialResourceTest {

    private static final String DEFAULT_MATERIAL_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_MATERIAL_NUMBER = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_MATERIAL_THICKNESS = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_MATERIAL_THICKNESS = BigDecimal.ONE;
    private static final String DEFAULT_MATERIAL_SIZE = "SAMPLE_TEXT";
    private static final String UPDATED_MATERIAL_SIZE = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_LB_PER_SHEET = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_LB_PER_SHEET = BigDecimal.ONE;

    private static final BigDecimal DEFAULT_DOLLAR_PER_LB = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_DOLLAR_PER_LB = BigDecimal.ONE;

    private static final Integer DEFAULT_INVENTORY_COUNT = 0;
    private static final Integer UPDATED_INVENTORY_COUNT = 1;

    @Inject
    private MaterialRepository materialRepository;

    private MockMvc restMaterialMockMvc;

    private Material material;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MaterialResource materialResource = new MaterialResource();
        ReflectionTestUtils.setField(materialResource, "materialRepository", materialRepository);
        this.restMaterialMockMvc = MockMvcBuilders.standaloneSetup(materialResource).build();
    }

    @Before
    public void initTest() {
        material = new Material();
        material.setMaterial_number(DEFAULT_MATERIAL_NUMBER);
        material.setMaterial_thickness(DEFAULT_MATERIAL_THICKNESS);
        material.setMaterial_size(DEFAULT_MATERIAL_SIZE);
        material.setLb_per_sheet(DEFAULT_LB_PER_SHEET);
        material.setDollar_per_lb(DEFAULT_DOLLAR_PER_LB);
        material.setInventory_count(DEFAULT_INVENTORY_COUNT);
    }

    @Test
    @Transactional
    public void createMaterial() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();

        // Create the Material
        restMaterialMockMvc.perform(post("/api/materials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(material)))
                .andExpect(status().isCreated());

        // Validate the Material in the database
        List<Material> materials = materialRepository.findAll();
        assertThat(materials).hasSize(databaseSizeBeforeCreate + 1);
        Material testMaterial = materials.get(materials.size() - 1);
        assertThat(testMaterial.getMaterial_number()).isEqualTo(DEFAULT_MATERIAL_NUMBER);
        assertThat(testMaterial.getMaterial_thickness()).isEqualTo(DEFAULT_MATERIAL_THICKNESS);
        assertThat(testMaterial.getMaterial_size()).isEqualTo(DEFAULT_MATERIAL_SIZE);
        assertThat(testMaterial.getLb_per_sheet()).isEqualTo(DEFAULT_LB_PER_SHEET);
        assertThat(testMaterial.getDollar_per_lb()).isEqualTo(DEFAULT_DOLLAR_PER_LB);
        assertThat(testMaterial.getInventory_count()).isEqualTo(DEFAULT_INVENTORY_COUNT);
    }

    @Test
    @Transactional
    public void getAllMaterials() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materials
        restMaterialMockMvc.perform(get("/api/materials"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
                .andExpect(jsonPath("$.[*].material_number").value(hasItem(DEFAULT_MATERIAL_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].material_thickness").value(hasItem(DEFAULT_MATERIAL_THICKNESS.intValue())))
                .andExpect(jsonPath("$.[*].material_size").value(hasItem(DEFAULT_MATERIAL_SIZE.toString())))
                .andExpect(jsonPath("$.[*].lb_per_sheet").value(hasItem(DEFAULT_LB_PER_SHEET.intValue())))
                .andExpect(jsonPath("$.[*].dollar_per_lb").value(hasItem(DEFAULT_DOLLAR_PER_LB.intValue())))
                .andExpect(jsonPath("$.[*].inventory_count").value(hasItem(DEFAULT_INVENTORY_COUNT)));
    }

    @Test
    @Transactional
    public void getMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.material_number").value(DEFAULT_MATERIAL_NUMBER.toString()))
            .andExpect(jsonPath("$.material_thickness").value(DEFAULT_MATERIAL_THICKNESS.intValue()))
            .andExpect(jsonPath("$.material_size").value(DEFAULT_MATERIAL_SIZE.toString()))
            .andExpect(jsonPath("$.lb_per_sheet").value(DEFAULT_LB_PER_SHEET.intValue()))
            .andExpect(jsonPath("$.dollar_per_lb").value(DEFAULT_DOLLAR_PER_LB.intValue()))
            .andExpect(jsonPath("$.inventory_count").value(DEFAULT_INVENTORY_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);
		
		int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material
        material.setMaterial_number(UPDATED_MATERIAL_NUMBER);
        material.setMaterial_thickness(UPDATED_MATERIAL_THICKNESS);
        material.setMaterial_size(UPDATED_MATERIAL_SIZE);
        material.setLb_per_sheet(UPDATED_LB_PER_SHEET);
        material.setDollar_per_lb(UPDATED_DOLLAR_PER_LB);
        material.setInventory_count(UPDATED_INVENTORY_COUNT);
        restMaterialMockMvc.perform(put("/api/materials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(material)))
                .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materials = materialRepository.findAll();
        assertThat(materials).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materials.get(materials.size() - 1);
        assertThat(testMaterial.getMaterial_number()).isEqualTo(UPDATED_MATERIAL_NUMBER);
        assertThat(testMaterial.getMaterial_thickness()).isEqualTo(UPDATED_MATERIAL_THICKNESS);
        assertThat(testMaterial.getMaterial_size()).isEqualTo(UPDATED_MATERIAL_SIZE);
        assertThat(testMaterial.getLb_per_sheet()).isEqualTo(UPDATED_LB_PER_SHEET);
        assertThat(testMaterial.getDollar_per_lb()).isEqualTo(UPDATED_DOLLAR_PER_LB);
        assertThat(testMaterial.getInventory_count()).isEqualTo(UPDATED_INVENTORY_COUNT);
    }

    @Test
    @Transactional
    public void deleteMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);
		
		int databaseSizeBeforeDelete = materialRepository.findAll().size();

        // Get the material
        restMaterialMockMvc.perform(delete("/api/materials/{id}", material.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Material> materials = materialRepository.findAll();
        assertThat(materials).hasSize(databaseSizeBeforeDelete - 1);
    }
}
