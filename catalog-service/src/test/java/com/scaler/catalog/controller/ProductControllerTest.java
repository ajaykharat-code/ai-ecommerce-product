package com.scaler.catalog.controller;

import com.scaler.catalog.entity.Product;
import com.scaler.catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        Product p = new Product();
        p.setName("Cache Test Item");
        p.setPrice(new BigDecimal("10.00"));
        p.setCategory("Test");
        productRepository.save(p);
    }

    @Test
    public void testGetProductsTriggersCache() throws Exception {
        // First call will hit DB and serialize to Redis
        mockMvc.perform(get("/catalog/products?page=0&size=10"))
                .andExpect(status().isOk());

        // Second call will hit Redis cache. If it isn't Serializable, this will crash.
        mockMvc.perform(get("/catalog/products?page=0&size=10"))
                .andExpect(status().isOk());
    }
}
