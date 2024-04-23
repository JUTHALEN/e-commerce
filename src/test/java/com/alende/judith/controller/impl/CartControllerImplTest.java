package com.alende.judith.controller.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;

import com.alende.judith.dto.CartResponse;
import com.alende.judith.dto.ProductRequest;
import com.alende.judith.model.Cart;
import com.alende.judith.model.Product;
import com.alende.judith.service.CartService;

class CartControllerImplTest {

    @InjectMocks
    CartControllerImpl cartController;

    CartService cartService;

    ProductRequest validProductRequest;

    Product product;

    Cart cart;

    @BeforeEach
    public void setup() {

        cartService = mock(CartService.class);

        cartController = new CartControllerImpl();
        cartController.cartService = cartService;

        validProductRequest = new ProductRequest(1, "Product Description", 10.0);
        product = new Product(1, "Product Description", 10.0);
        cart = new Cart(1L, Map.of(1, product), LocalDateTime.now().plusMinutes(10));
    }

    @Test
    public void testCreateCart_HappyCase() {

        List<ProductRequest> productRequests = List.of(validProductRequest);
        when(cartService.createCart(anyList())).thenReturn(cart);

        ResponseEntity<CartResponse> response = cartController.createCart(productRequests);

        assertNotNull(response);
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(cart.getId(), response.getBody().getId());

        verify(cartService).createCart(anyList());
    }

    @Test
    public void testCreateCart_ThrowsException_ProductNull() {

        ResponseEntity<CartResponse> response = cartController.createCart(null);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateCart_ShouldReturnBadRequest_ProductRequestListIsEmpty() {

        ResponseEntity<CartResponse> response = cartController.createCart(Collections.emptyList());

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateCart_BadRequest_ProductRequestIsInvalid() {

        ProductRequest invalidProductRequest = new ProductRequest(-1, " ", -10.0);
        List<ProductRequest> productRequests = List.of(validProductRequest, invalidProductRequest);

        ResponseEntity<CartResponse> response = cartController.createCart(productRequests);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetCartById_HappyCase() {

        when(cartService.getCartById(anyLong())).thenReturn(cart);

        ResponseEntity<CartResponse> response = cartController.getCartById(1L);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(cart.getId(), Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    public void getCartById_BadRequest_CartDoesNotExist() {

        when(cartService.getCartById(anyLong())).thenReturn(null);

        ResponseEntity<CartResponse> response = cartController.getCartById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testAddProductToCart_HappyCase() {

        when(cartService.getCartById(anyLong())).thenReturn(cart);
        when(cartService.addProductToCart(anyLong(), any())).thenReturn(cart);

        ResponseEntity<CartResponse> response = cartController.addProductToCart(1L, validProductRequest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(cart.getId(), Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    public void testAddProductToCart_BadRequest_CartNull() {

        when(cartService.getCartById(anyLong())).thenReturn(null);

        ResponseEntity<CartResponse> response = cartController.addProductToCart(1L, validProductRequest);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testAddProductToCart_BadRequest_ProductRequestNull() {

        ResponseEntity<CartResponse> response = cartController.addProductToCart(1L, null);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testAddProductToCart_BadRequest_ProductRequestIsInvalid() {

        ProductRequest invalidProductRequest = new ProductRequest(-1, " ", -10.0);

        ResponseEntity<CartResponse> response = cartController.addProductToCart(1L, invalidProductRequest);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testRemoveProductFromCart_HappyCase() {

        when(cartService.getCartById(anyLong())).thenReturn(cart);
        when(cartService.removeProductFromCart(anyLong(), anyInt())).thenReturn(cart);

        ResponseEntity<CartResponse> response = cartController.removeProductFromCart(1L, 1);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(cart.getId(), Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    public void testRemoveProductFromCart_BadRequest_CartNull() {

        when(cartService.getCartById(anyLong())).thenReturn(null);

        ResponseEntity<CartResponse> response = cartController.removeProductFromCart(1L, 1);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testRemoveProductFromCart_BadRequest_ProductIdInvalid() {

        when(cartService.getCartById(anyLong())).thenReturn(cart);

        ResponseEntity<CartResponse> response = cartController.removeProductFromCart(1L, -1);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteCart_HappyCase() {

        when(cartService.getCartById(anyLong())).thenReturn(cart);

        ResponseEntity<Void> response = cartController.deleteCart(1L);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testDeleteCart_BadRequest_CartNull() {

        when(cartService.getCartById(anyLong())).thenReturn(null);

        ResponseEntity<Void> response = cartController.deleteCart(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }
}
