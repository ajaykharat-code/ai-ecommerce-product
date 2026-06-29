package com.scaler.order.dto;

import com.scaler.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Order order;
    private String checkoutUrl;
}
