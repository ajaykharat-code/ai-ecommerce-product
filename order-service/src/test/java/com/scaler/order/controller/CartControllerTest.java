package com.scaler.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.order.dto.CartItemRequest;
import com.scaler.order.entity.Cart;
import com.scaler.order.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetCart() throws Exception {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUserId("user-123");

        when(cartService.getCartByUserId("user-123")).thenReturn(cart);

        mockMvc.perform(get("/api/cart")
                .header("X-User-Id", "user-123"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddItem() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setProductId(UUID.randomUUID());
        request.setQuantity(2);

        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());

        when(cartService.addItemToCart(eq("user-123"), any(), eq(2))).thenReturn(cart);

        mockMvc.perform(post("/api/cart/items")
                .header("X-User-Id", "user-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
