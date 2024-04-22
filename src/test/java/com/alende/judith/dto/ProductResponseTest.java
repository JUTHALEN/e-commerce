package com.alende.judith.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.alende.judith.model.Product;

class ProductResponseTest {

    @Test
    public void testConstructorWithProduct() {

        Product product = new Product(1, "Test Product", 10.0);
        ProductResponse response = new ProductResponse(product);

        assertEquals(product.getId(), response.getId());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getAmount(), response.getAmount());
    }
}