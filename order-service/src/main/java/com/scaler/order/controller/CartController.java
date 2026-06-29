package com.scaler.order.controller;

import com.scaler.order.dto.CartItemRequest;
import com.scaler.order.entity.Cart;
import com.scaler.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, request.getProductId(), request.getQuantity()));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Cart> removeItem(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable UUID productId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, productId));
    }
}
