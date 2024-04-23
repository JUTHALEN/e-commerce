package com.alende.judith.controller.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;

import com.alende.judith.dto.ProductRequest;
import com.alende.judith.dto.ProductResponse;
import com.alende.judith.model.Product;
import com.alende.judith.service.ProductService;

class ProductControllerImplTest {
    @InjectMocks
    ProductControllerImpl productController;

    ProductService service;

    Product product;

    ProductRequest validProductRequest;

    @BeforeEach
    public void setup() {

        service = mock(ProductService.class);

        productController = new ProductControllerImpl();
        productController.productService = service;

        validProductRequest = new ProductRequest(1, "Product Description", 10.0);
        product = new Product(1, "Product Description", 10.0);
    }

    @Test
    public void testCreateProduct_HappyCase() {

        when(service.createProduct(anyString(), anyDouble())).thenReturn(product);

        ResponseEntity<ProductResponse> response = productController.createProduct(validProductRequest);

        assertNotNull(response);
        assertEquals(CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateProduct_InvalidRequest() {

        validProductRequest.setDescription(null);

        ResponseEntity<ProductResponse> response = productController.createProduct(validProductRequest);

        assertNotNull(response);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetProductById_HappyCase() {

        when(service.getProductById(1)).thenReturn(product);

        ResponseEntity<ProductResponse> response = productController.getProductById(1);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testGetProductById_InvalidRequest() {

        when(service.getProductById(1)).thenReturn(null);

        ResponseEntity<ProductResponse> response = productController.getProductById(1);

        assertNotNull(response);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetAllProducts_HappyCase() {

        when(service.getProducts()).thenReturn(List.of(product));

        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct_HappyCase() {

        when(service.updateProduct(1, product)).thenReturn(product);

        ResponseEntity<ProductResponse> response = productController.updateProduct(1, validProductRequest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct_InvalidRequest() {

        validProductRequest.setDescription(null);

        ResponseEntity<ProductResponse> response = productController.updateProduct(1, validProductRequest);

        assertNotNull(response);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct_NotFound() {

        when(service.updateProduct(1, product)).thenReturn(null);

        ResponseEntity<ProductResponse> response = productController.updateProduct(1, validProductRequest);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteProduct_HappyCase() {

        ResponseEntity<Void> response = productController.deleteProduct(1);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
    }
}
