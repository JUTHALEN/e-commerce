package com.alende.judith.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductRequestTest {

    @Test
    public void testProductRequestConstructorAndGetters() {

        ProductRequest productRequest = new ProductRequest(1, "Product 1", 10.0);

        assertEquals(1, productRequest.getId());
        assertEquals("Product 1", productRequest.getDescription());
        assertEquals(10.0, productRequest.getAmount());
    }

    @Test
    public void testProductRequestSetters() {

        ProductRequest productRequest = new ProductRequest();

        productRequest.setId(2);
        productRequest.setDescription("Product 2");
        productRequest.setAmount(20.0);

        assertEquals(2, productRequest.getId());
        assertEquals("Product 2", productRequest.getDescription());
        assertEquals(20.0, productRequest.getAmount());
    }
}
