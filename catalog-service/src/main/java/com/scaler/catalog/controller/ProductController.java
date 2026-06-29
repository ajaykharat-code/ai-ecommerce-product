package com.scaler.catalog.controller;

import com.scaler.catalog.entity.Product;
import com.scaler.catalog.repository.ProductRepository;
import com.scaler.catalog.service.GeminiSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GeminiSearchService geminiSearchService;

    // Story 2.2: Paginated & Cached Product Listing
    @GetMapping
    @Cacheable(value = "products", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Story 2.3 & 2.4: Semantic Search & SQL Fallback
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String q) {
        // Step 1: AI Semantic extraction
        String enhancedKeywords = geminiSearchService.enhanceSearchQuery(q);
        
        // Step 2: SQL Search using the extracted keywords
        return productRepository.searchByKeyword(enhancedKeywords);
    }
    
    // Internal use for testing/seeding
    @PostMapping
    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}
