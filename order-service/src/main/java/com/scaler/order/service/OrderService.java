package com.scaler.order.service;

import com.scaler.order.entity.Cart;
import com.scaler.order.entity.CartItem;
import com.scaler.order.entity.Order;
import com.scaler.order.entity.OrderItem;
import com.scaler.order.exception.ResourceNotFoundException;
import com.scaler.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import com.scaler.order.dto.OrderResponse;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final RestTemplate restTemplate;

    @Transactional
    public OrderResponse checkout(String userId) {
        Cart cart = cartService.getCartByUserId(userId);
        
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = Order.builder()
                .userId(userId)
                .status("PENDING")
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            BigDecimal price = new BigDecimal("100.00");
            OrderItem orderItem = OrderItem.builder()
                    .productId(cartItem.getProductId())
                    .quantity(cartItem.getQuantity())
                    .price(price)
                    .build();
            order.addItem(orderItem);
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotalAmount(totalAmount);
        cart.getItems().clear();
        order = orderRepository.save(order);

        // Call payment-service to get checkout URL
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("orderId", order.getId());
        paymentRequest.put("amount", order.getTotalAmount());
        
        String checkoutUrl = null;
        try {
            Map response = restTemplate.postForObject(
                "http://payment-service/api/payments/create-checkout-session", 
                paymentRequest, 
                Map.class
            );
            if (response != null && response.containsKey("checkoutUrl")) {
                checkoutUrl = (String) response.get("checkoutUrl");
            }
        } catch (Exception e) {
            // Log error, continue with order creation but without URL
            System.err.println("Failed to create checkout session: " + e.getMessage());
        }

        return new OrderResponse(order, checkoutUrl);
    }
}
