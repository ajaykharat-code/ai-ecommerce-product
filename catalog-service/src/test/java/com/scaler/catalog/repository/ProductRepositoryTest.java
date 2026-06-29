package com.scaler.catalog.repository;

import com.scaler.catalog.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveAndFindProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Test Boots");
        product.setDescription("Waterproof");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(10);
        product.setCategory("Footwear");

        // Act
        Product savedProduct = productRepository.save(product);
        Optional<Product> retrievedProduct = productRepository.findById(savedProduct.getId());

        // Assert
        assertThat(retrievedProduct).isPresent();
        assertThat(retrievedProduct.get().getName()).isEqualTo("Test Boots");
        assertThat(retrievedProduct.get().getPrice()).isEqualTo(new BigDecimal("99.99"));
    }
}
