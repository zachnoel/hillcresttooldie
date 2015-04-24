package com.htd.web.rest;

import com.htd.Application;
import com.htd.domain.Po;
import com.htd.repository.PoRepository;

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
import org.joda.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PoResource REST controller.
 *
 * @see PoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PoResourceTest {

    private static final String DEFAULT_PO_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_PO_NUMBER = "UPDATED_TEXT";
    private static final String DEFAULT_SALES_ORDER_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_SALES_ORDER_NUMBER = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_DUE_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_DUE_DATE = new LocalDate();
    private static final String DEFAULT_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_STATUS = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_TOTAL_SALE = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_TOTAL_SALE = BigDecimal.ONE;

    @Inject
    private PoRepository poRepository;

    private MockMvc restPoMockMvc;

    private Po po;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PoResource poResource = new PoResource();
        ReflectionTestUtils.setField(poResource, "poRepository", poRepository);
        this.restPoMockMvc = MockMvcBuilders.standaloneSetup(poResource).build();
    }

    @Before
    public void initTest() {
        po = new Po();
        po.setPo_number(DEFAULT_PO_NUMBER);
        po.setSales_order_number(DEFAULT_SALES_ORDER_NUMBER);
        po.setDue_date(DEFAULT_DUE_DATE);
        po.setStatus(DEFAULT_STATUS);
        po.setTotal_sale(DEFAULT_TOTAL_SALE);
    }

    @Test
    @Transactional
    public void createPo() throws Exception {
        int databaseSizeBeforeCreate = poRepository.findAll().size();

        // Create the Po
        restPoMockMvc.perform(post("/api/pos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(po)))
                .andExpect(status().isCreated());

        // Validate the Po in the database
        List<Po> pos = poRepository.findAll();
        assertThat(pos).hasSize(databaseSizeBeforeCreate + 1);
        Po testPo = pos.get(pos.size() - 1);
        assertThat(testPo.getPo_number()).isEqualTo(DEFAULT_PO_NUMBER);
        assertThat(testPo.getSales_order_number()).isEqualTo(DEFAULT_SALES_ORDER_NUMBER);
        assertThat(testPo.getDue_date()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testPo.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPo.getTotal_sale()).isEqualTo(DEFAULT_TOTAL_SALE);
    }

    @Test
    @Transactional
    public void getAllPos() throws Exception {
        // Initialize the database
        poRepository.saveAndFlush(po);

        // Get all the pos
        restPoMockMvc.perform(get("/api/pos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(po.getId().intValue())))
                .andExpect(jsonPath("$.[*].po_number").value(hasItem(DEFAULT_PO_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].sales_order_number").value(hasItem(DEFAULT_SALES_ORDER_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].due_date").value(hasItem(DEFAULT_DUE_DATE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].total_sale").value(hasItem(DEFAULT_TOTAL_SALE.intValue())));
    }

    @Test
    @Transactional
    public void getPo() throws Exception {
        // Initialize the database
        poRepository.saveAndFlush(po);

        // Get the po
        restPoMockMvc.perform(get("/api/pos/{id}", po.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(po.getId().intValue()))
            .andExpect(jsonPath("$.po_number").value(DEFAULT_PO_NUMBER.toString()))
            .andExpect(jsonPath("$.sales_order_number").value(DEFAULT_SALES_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.due_date").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.total_sale").value(DEFAULT_TOTAL_SALE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPo() throws Exception {
        // Get the po
        restPoMockMvc.perform(get("/api/pos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePo() throws Exception {
        // Initialize the database
        poRepository.saveAndFlush(po);
		
		int databaseSizeBeforeUpdate = poRepository.findAll().size();

        // Update the po
        po.setPo_number(UPDATED_PO_NUMBER);
        po.setSales_order_number(UPDATED_SALES_ORDER_NUMBER);
        po.setDue_date(UPDATED_DUE_DATE);
        po.setStatus(UPDATED_STATUS);
        po.setTotal_sale(UPDATED_TOTAL_SALE);
        restPoMockMvc.perform(put("/api/pos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(po)))
                .andExpect(status().isOk());

        // Validate the Po in the database
        List<Po> pos = poRepository.findAll();
        assertThat(pos).hasSize(databaseSizeBeforeUpdate);
        Po testPo = pos.get(pos.size() - 1);
        assertThat(testPo.getPo_number()).isEqualTo(UPDATED_PO_NUMBER);
        assertThat(testPo.getSales_order_number()).isEqualTo(UPDATED_SALES_ORDER_NUMBER);
        assertThat(testPo.getDue_date()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testPo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPo.getTotal_sale()).isEqualTo(UPDATED_TOTAL_SALE);
    }

    @Test
    @Transactional
    public void deletePo() throws Exception {
        // Initialize the database
        poRepository.saveAndFlush(po);
		
		int databaseSizeBeforeDelete = poRepository.findAll().size();

        // Get the po
        restPoMockMvc.perform(delete("/api/pos/{id}", po.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Po> pos = poRepository.findAll();
        assertThat(pos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
