package com.scaler.order.controller;

import com.scaler.order.entity.Order;
import com.scaler.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.scaler.order.dto.OrderResponse;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(orderService.checkout(userId));
    }
}
