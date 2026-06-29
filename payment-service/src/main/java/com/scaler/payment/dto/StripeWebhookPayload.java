package com.scaler.payment.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class StripeWebhookPayload {
    private String stripePaymentId;
    private UUID orderId;
    private BigDecimal amount;
    private String status;
}
