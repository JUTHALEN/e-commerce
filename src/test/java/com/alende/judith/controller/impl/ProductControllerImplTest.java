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
        // Arrange
        when(service.createProduct(anyString(), anyDouble())).thenReturn(product);

        // Act
        ResponseEntity<ProductResponse> response = productController.createProduct(validProductRequest);

        // Assert
        assertNotNull(response);
        assertEquals(CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateProduct_InvalidRequest() {
        // Arrange
        validProductRequest.setDescription(null);

        // Act
        ResponseEntity<ProductResponse> response = productController.createProduct(validProductRequest);

        // Assert
        assertNotNull(response);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetProductById_HappyCase() {
        // Arrange
        when(service.getProductById(1)).thenReturn(product);

        // Act
        ResponseEntity<ProductResponse> response = productController.getProductById(1);

        // Assert
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testGetProductById_InvalidRequest() {
        // Arrange
        when(service.getProductById(1)).thenReturn(null);

        // Act
        ResponseEntity<ProductResponse> response = productController.getProductById(1);

        // Assert
        assertNotNull(response);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetAllProducts_HappyCase() {
        // Arrange
        when(service.getProducts()).thenReturn(List.of(product));

        // Act
        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();

        // Assert
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct_HappyCase() {
        // Arrange
        when(service.updateProduct(1, product)).thenReturn(product);

        // Act
        ResponseEntity<ProductResponse> response = productController.updateProduct(1, validProductRequest);

        // Assert
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct_InvalidRequest() {
        // Arrange
        validProductRequest.setDescription(null);

        // Act
        ResponseEntity<ProductResponse> response = productController.updateProduct(1, validProductRequest);

        // Assert
        assertNotNull(response);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct_NotFound() {
        // Arrange
        when(service.updateProduct(1, product)).thenReturn(null);

        // Act
        ResponseEntity<ProductResponse> response = productController.updateProduct(1, validProductRequest);

        // Assert
        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteProduct_HappyCase() {
        // Arrange
        // Act
        ResponseEntity<Void> response = productController.deleteProduct(1);

        // Assert
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
    }
}
