package com.alende.judith.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alende.judith.dto.CartResponse;
import com.alende.judith.dto.ProductRequest;

public interface CartController {

    @PostMapping("/api/cart")
    ResponseEntity<CartResponse> createCart(@RequestBody List<ProductRequest> productRequests);

    @GetMapping("/api/cart/{id}")
    ResponseEntity<CartResponse> getCartById(@PathVariable("id") Long cartId);

    @PostMapping("/api/cart/{id}/products")
    ResponseEntity<CartResponse> addProductToCart(@PathVariable("id") Long cartId, @RequestBody ProductRequest product);

    @DeleteMapping("/api/cart/{id}/products/{productId}")
    ResponseEntity<CartResponse> removeProductFromCart(@PathVariable("id") Long cartId, @PathVariable("productId") Integer productId);

    @DeleteMapping("/api/cart/{id}")
    ResponseEntity<Void> deleteCart(@PathVariable("id") Long cartId);
}