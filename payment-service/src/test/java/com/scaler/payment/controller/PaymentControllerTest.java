package com.scaler.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.payment.dto.PaymentRequest;
import com.scaler.payment.service.StripeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.scaler.payment.dto.StripeWebhookPayload;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StripeService stripeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateSession() throws Exception {
        UUID orderId = UUID.randomUUID();
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(orderId);
        request.setAmount(new BigDecimal("100.00"));

        String mockUrl = "https://checkout.stripe.com/pay/mock_session_" + orderId;
        when(stripeService.createCheckoutSession(orderId)).thenReturn(mockUrl);

        mockMvc.perform(post("/api/payments/create-checkout-session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkoutUrl").value(mockUrl));
    }

    @Test
    void shouldProcessWebhook() throws Exception {
        StripeWebhookPayload payload = new StripeWebhookPayload();
        payload.setOrderId(UUID.randomUUID());
        payload.setStripePaymentId("pi_test_123");
        payload.setAmount(new BigDecimal("100.00"));
        payload.setStatus("SUCCESS");

        mockMvc.perform(post("/api/payments/webhook")
                .header("Stripe-Signature", "valid-signature")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectInvalidWebhook() throws Exception {
        StripeWebhookPayload payload = new StripeWebhookPayload();
        
        doThrow(new IllegalArgumentException("Invalid Stripe signature"))
            .when(stripeService).handleWebhook(any(), any());

        mockMvc.perform(post("/api/payments/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }
}
