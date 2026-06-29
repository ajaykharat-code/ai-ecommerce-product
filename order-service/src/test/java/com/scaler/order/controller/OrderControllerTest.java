package com.scaler.order.controller;

import com.scaler.order.entity.Order;
import com.scaler.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.scaler.order.dto.OrderResponse;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldCheckout() throws Exception {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUserId("user-123");
        order.setStatus("PENDING");
        order.setTotalAmount(new BigDecimal("100.00"));

        OrderResponse response = new OrderResponse(order, "http://mock");
        when(orderService.checkout("user-123")).thenReturn(response);

        mockMvc.perform(post("/api/orders/checkout")
                .header("X-User-Id", "user-123"))
                .andExpect(status().isOk());
    }
}
