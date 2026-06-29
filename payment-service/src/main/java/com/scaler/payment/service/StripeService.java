package com.scaler.payment.service;

import com.scaler.payment.entity.PaymentTransaction;
import com.scaler.payment.repository.PaymentTransactionRepository;
import com.scaler.payment.dto.StripeWebhookPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StripeService {

    private final PaymentTransactionRepository transactionRepository;

    public String createCheckoutSession(UUID orderId) {
        return "https://checkout.stripe.com/pay/mock_session_" + orderId.toString();
    }

    public void handleWebhook(String signature, StripeWebhookPayload payload) {
        if (signature == null || signature.isEmpty()) {
            throw new IllegalArgumentException("Invalid Stripe signature");
        }
        
        PaymentTransaction transaction = PaymentTransaction.builder()
                .orderId(payload.getOrderId())
                .stripePaymentId(payload.getStripePaymentId())
                .status(payload.getStatus())
                .amount(payload.getAmount())
                .build();
                
        transactionRepository.save(transaction);
    }
}
