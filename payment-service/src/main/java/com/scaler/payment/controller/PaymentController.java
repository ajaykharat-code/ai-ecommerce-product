package com.scaler.payment.controller;

import com.scaler.payment.dto.PaymentRequest;
import com.scaler.payment.service.StripeService;
import lombok.RequiredArgsConstructor;
import com.scaler.payment.dto.StripeWebhookPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final StripeService stripeService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createSession(@RequestBody PaymentRequest request) {
        String checkoutUrl = stripeService.createCheckoutSession(request.getOrderId());
        return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader(value = "Stripe-Signature", required = false) String signature,
            @RequestBody StripeWebhookPayload payload) {
        try {
            stripeService.handleWebhook(signature, payload);
            return ResponseEntity.ok("Webhook processed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
