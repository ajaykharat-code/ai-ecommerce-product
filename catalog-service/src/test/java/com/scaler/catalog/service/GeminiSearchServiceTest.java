package com.scaler.catalog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GeminiSearchServiceTest {

    @Autowired
    private GeminiSearchService geminiSearchService;

    @Test
    public void testEnhanceSearchQuery_FallbackToOriginal() {
        // Since the API key is "dummy_key", the resilience fallback should kick in instantly.
        String originalQuery = "shoes for walking in the rain";
        String enhancedQuery = geminiSearchService.enhanceSearchQuery(originalQuery);

        // Assert that the fallback mechanism returns the original string safely without crashing
        assertThat(enhancedQuery).isEqualTo(originalQuery);
    }
}
