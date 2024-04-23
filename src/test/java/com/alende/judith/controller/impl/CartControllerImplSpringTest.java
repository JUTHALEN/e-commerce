package com.alende.judith.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.alende.judith.config.ScheduledTasks;
import com.alende.judith.dto.CartResponse;
import com.alende.judith.dto.ProductRequest;
import com.alende.judith.model.Cart;
import com.alende.judith.model.Product;
import com.alende.judith.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CartControllerImpl.class)
@ImportAutoConfiguration(classes = { ScheduledTasks.class })
public class CartControllerImplSpringTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CartService cartService;

    ProductRequest validProductRequest;

    Product product;

    Cart cart;

    LocalDateTime fixedDateTime;

    @BeforeEach
    public void setup() {

        fixedDateTime = LocalDateTime.of(2024, 4, 22, 12, 0, 0, 0);

        validProductRequest = new ProductRequest(1, "Product Description", 10.0);
        product = new Product(1, "Product Description", 10.0);
        cart = new Cart(1L, Map.of(1, product), fixedDateTime.plusMinutes(10));
    }

    @Test
    void testCreateCart() throws Exception {

        List<ProductRequest> productRequests = Collections.singletonList(validProductRequest);

        when(cartService.createCart(any())).thenReturn(cart);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/cart").content(objectMapper.writeValueAsString(productRequests))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpect(
                content().string(
                        "{\"id\":1,\"products\":{\"1\":{\"id\":1,\"description\":\"Product Description\",\"amount\":10.0}},\"expiryTime\":\"2024-04-22T12:10:00\"}"));
    }

    @Test
    void testGetCartById_WhenCartExists() throws Exception {

        when(cartService.getCartById(1L)).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(
                        "{\"id\":1,\"products\":{\"1\":{\"id\":1,\"description\":\"Product Description\",\"amount\":10.0}},\"expiryTime\":\"2024-04-22T12:10:00\"}"));
    }

    @Test
    void testGetCartById_WhenCartDoesNotExist() throws Exception {

        when(cartService.getCartById(1L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddProductToCart_CartNotFound() throws Exception {

        when(cartService.getCartById(anyLong())).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/cart/{id}/products", 1L).contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddProductToCart_Success() throws Exception {
        when(cartService.getCartById(anyLong())).thenReturn(cart);
        when(cartService.addProductToCart(anyLong(), any())).thenReturn(cart);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/cart/{id}/products", 1L).contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validProductRequest))).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(new CartResponse(cart))));
    }

    @Test
    void testDeleteCart_CartExists() throws Exception {

        when(cartService.getCartById(anyLong())).thenReturn(cart);
        doNothing().when(cartService).deleteCart(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCart_CartNotFound() throws Exception {

        when(cartService.getCartById(anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
