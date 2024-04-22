package com.alende.judith.controller.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alende.judith.controller.CartController;
import com.alende.judith.dto.CartResponse;
import com.alende.judith.dto.ProductRequest;
import com.alende.judith.model.Cart;
import com.alende.judith.model.Product;
import com.alende.judith.service.CartService;

@RestController
public class CartControllerImpl implements CartController {

    @Autowired
    CartService cartService;

    @Override
    public ResponseEntity<CartResponse> createCart(@RequestBody List<ProductRequest> productRequests) {

        if(productRequests == null || productRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        for(ProductRequest pr : productRequests) {
            if(pr.getId() <= 0 || pr.getDescription() == null || pr.getDescription().trim()
                    .isEmpty() || pr.getAmount() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        final var products = productRequests.stream()
                .map(pr -> new Product(pr.getId(), pr.getDescription(), pr.getAmount()))
                .toList();
        final var cart = cartService.createCart(products);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CartResponse(cart));
    }

    @Override
    public ResponseEntity<CartResponse> getCartById(Long cartId) {

        Cart cart = cartService.getCartById(cartId);
        if(cart == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CartResponse(cart));
    }

    @Override
    public ResponseEntity<CartResponse> addProductToCart(Long cartId, ProductRequest productRequest) {

        if(productRequest == null || productRequest.getId() <= 0 || productRequest.getDescription() == null
                || productRequest.getDescription().isEmpty() || productRequest.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        final var cart = cartService.getCartById(cartId);
        if(cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        final var product = new Product(productRequest.getId(), productRequest.getDescription(),
                productRequest.getAmount());
        final var updatedCart = cartService.addProductToCart(cart.getId(), product);

        if(updatedCart.getProducts().size() == 1) {
            updatedCart.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CartResponse(updatedCart));
    }

    @Override
    public ResponseEntity<CartResponse> removeProductFromCart(Long cartId, Integer productId) {
        Cart cart = cartService.getCartById(cartId);
        if(cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart updatedCart = cartService.removeProductFromCart(cartId, productId);
        if(updatedCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CartResponse(updatedCart));
    }

    @Override
    public ResponseEntity<Void> deleteCart(Long cartId) {
        final var cart = cartService.getCartById(cartId);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        cartService.deleteCart(cartId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
