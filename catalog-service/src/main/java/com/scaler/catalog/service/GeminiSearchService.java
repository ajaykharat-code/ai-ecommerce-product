package com.scaler.catalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiSearchService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String enhanceSearchQuery(String query) {
        if ("dummy_key".equals(apiKey)) {
            return query; // Fallback immediately if no real key
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String prompt = "Extract ONLY the single most important core product name from this user query. For example, if the query is 'track pant for rain', output exactly 'track pant'. Drop all adjectives, conditions, or contexts like 'rain' or 'for'. Return ONLY the contiguous core product name, nothing else. Query: " + query;
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(
                    Map.of("parts", List.of(
                            Map.of("text", prompt)
                    ))
            ));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            String url = apiUrl + "?key=" + apiKey;
            String responseStr = restTemplate.postForObject(url, entity, String.class);
            
            JsonNode root = objectMapper.readTree(responseStr);
            String extracted = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText().trim();
            System.out.println("GEMINI EXTRACTED: [" + extracted + "]");
            return extracted;
            
        } catch (Exception e) {
            System.err.println("Gemini API Error (Falling back to SQL): " + e.getMessage());
            // Fallback Resilience (Story 2.4)
            return query; 
        }
    }
}
