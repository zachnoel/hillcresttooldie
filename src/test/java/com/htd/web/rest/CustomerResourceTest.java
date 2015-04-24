package com.htd.web.rest;

import com.htd.Application;
import com.htd.domain.Customer;
import com.htd.repository.CustomerRepository;

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
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerResourceTest {

    private static final String DEFAULT_CUSTOMER_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_CUSTOMER_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_CUSTOMER_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_CUSTOMER_ADDRESS = "UPDATED_TEXT";
    private static final String DEFAULT_CUSTOMER_PHONE = "SAMPLE_TEXT";
    private static final String UPDATED_CUSTOMER_PHONE = "UPDATED_TEXT";

    @Inject
    private CustomerRepository customerRepository;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerResource customerResource = new CustomerResource();
        ReflectionTestUtils.setField(customerResource, "customerRepository", customerRepository);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource).build();
    }

    @Before
    public void initTest() {
        customer = new Customer();
        customer.setCustomer_name(DEFAULT_CUSTOMER_NAME);
        customer.setCustomer_address(DEFAULT_CUSTOMER_ADDRESS);
        customer.setCustomer_phone(DEFAULT_CUSTOMER_PHONE);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getCustomer_name()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testCustomer.getCustomer_address()).isEqualTo(DEFAULT_CUSTOMER_ADDRESS);
        assertThat(testCustomer.getCustomer_phone()).isEqualTo(DEFAULT_CUSTOMER_PHONE);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customers
        restCustomerMockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
                .andExpect(jsonPath("$.[*].customer_name").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())))
                .andExpect(jsonPath("$.[*].customer_address").value(hasItem(DEFAULT_CUSTOMER_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].customer_phone").value(hasItem(DEFAULT_CUSTOMER_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.customer_name").value(DEFAULT_CUSTOMER_NAME.toString()))
            .andExpect(jsonPath("$.customer_address").value(DEFAULT_CUSTOMER_ADDRESS.toString()))
            .andExpect(jsonPath("$.customer_phone").value(DEFAULT_CUSTOMER_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
		
		int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        customer.setCustomer_name(UPDATED_CUSTOMER_NAME);
        customer.setCustomer_address(UPDATED_CUSTOMER_ADDRESS);
        customer.setCustomer_phone(UPDATED_CUSTOMER_PHONE);
        restCustomerMockMvc.perform(put("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getCustomer_name()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomer.getCustomer_address()).isEqualTo(UPDATED_CUSTOMER_ADDRESS);
        assertThat(testCustomer.getCustomer_phone()).isEqualTo(UPDATED_CUSTOMER_PHONE);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
		
		int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
