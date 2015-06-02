package com.htd.web.rest;

import com.htd.Application;
import com.htd.domain.Supplier;
import com.htd.repository.SupplierRepository;

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
 * Test class for the SupplierResource REST controller.
 *
 * @see SupplierResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SupplierResourceTest {

    private static final String DEFAULT_SUPPLIER_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_SUPPLIER_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_SUPPLIER_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_SUPPLIER_ADDRESS = "UPDATED_TEXT";
    private static final String DEFAULT_SUPPLIER_PHONE = "SAMPLE_TEXT";
    private static final String UPDATED_SUPPLIER_PHONE = "UPDATED_TEXT";

    @Inject
    private SupplierRepository supplierRepository;

    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SupplierResource supplierResource = new SupplierResource();
        ReflectionTestUtils.setField(supplierResource, "supplierRepository", supplierRepository);
        this.restSupplierMockMvc = MockMvcBuilders.standaloneSetup(supplierResource).build();
    }

    @Before
    public void initTest() {
        supplier = new Supplier();
        supplier.setSupplier_name(DEFAULT_SUPPLIER_NAME);
        supplier.setSupplier_address(DEFAULT_SUPPLIER_ADDRESS);
        supplier.setSupplier_phone(DEFAULT_SUPPLIER_PHONE);
    }

    @Test
    @Transactional
    public void createSupplier() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();

        // Create the Supplier
        restSupplierMockMvc.perform(post("/api/suppliers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplier)))
                .andExpect(status().isCreated());

        // Validate the Supplier in the database
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeCreate + 1);
        Supplier testSupplier = suppliers.get(suppliers.size() - 1);
        assertThat(testSupplier.getSupplier_name()).isEqualTo(DEFAULT_SUPPLIER_NAME);
        assertThat(testSupplier.getSupplier_address()).isEqualTo(DEFAULT_SUPPLIER_ADDRESS);
        assertThat(testSupplier.getSupplier_phone()).isEqualTo(DEFAULT_SUPPLIER_PHONE);
    }

    @Test
    @Transactional
    public void checkSupplier_nameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(supplierRepository.findAll()).hasSize(0);
        // set the field null
        supplier.setSupplier_name(null);

        // Create the Supplier, which fails.
        restSupplierMockMvc.perform(post("/api/suppliers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplier)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllSuppliers() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the suppliers
        restSupplierMockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
                .andExpect(jsonPath("$.[*].supplier_name").value(hasItem(DEFAULT_SUPPLIER_NAME.toString())))
                .andExpect(jsonPath("$.[*].supplier_address").value(hasItem(DEFAULT_SUPPLIER_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].supplier_phone").value(hasItem(DEFAULT_SUPPLIER_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.supplier_name").value(DEFAULT_SUPPLIER_NAME.toString()))
            .andExpect(jsonPath("$.supplier_address").value(DEFAULT_SUPPLIER_ADDRESS.toString()))
            .andExpect(jsonPath("$.supplier_phone").value(DEFAULT_SUPPLIER_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);
		
		int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier
        supplier.setSupplier_name(UPDATED_SUPPLIER_NAME);
        supplier.setSupplier_address(UPDATED_SUPPLIER_ADDRESS);
        supplier.setSupplier_phone(UPDATED_SUPPLIER_PHONE);
        restSupplierMockMvc.perform(put("/api/suppliers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplier)))
                .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = suppliers.get(suppliers.size() - 1);
        assertThat(testSupplier.getSupplier_name()).isEqualTo(UPDATED_SUPPLIER_NAME);
        assertThat(testSupplier.getSupplier_address()).isEqualTo(UPDATED_SUPPLIER_ADDRESS);
        assertThat(testSupplier.getSupplier_phone()).isEqualTo(UPDATED_SUPPLIER_PHONE);
    }

    @Test
    @Transactional
    public void deleteSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);
		
		int databaseSizeBeforeDelete = supplierRepository.findAll().size();

        // Get the supplier
        restSupplierMockMvc.perform(delete("/api/suppliers/{id}", supplier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
