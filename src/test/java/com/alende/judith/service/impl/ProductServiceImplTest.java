package com.alende.judith.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alende.judith.model.Product;

class ProductServiceImplTest {

    ProductServiceImpl service;

    Product product;

    @BeforeEach
    void setUp() {

        service = new ProductServiceImpl();
        product = new Product(1, "Product 1", 10.0);
        service.createProduct(product.getDescription(), product.getAmount());
    }

    @Test
    void testCreateProduct_HappyCase() {

        assertNotNull(product);
        assertEquals(1, product.getId());
        assertEquals("Product 1", product.getDescription());
        assertEquals(10.0, product.getAmount());
    }

    @Test
    void testCreateProduct_FailureNegativeAmount() {

        var exception = assertThrows(IllegalArgumentException.class, () -> service.createProduct("Product 1", -10.0));
        assertEquals("Product amount must not be negative", exception.getMessage());
    }

    @Test
    void testGetProductById_HappyCase() {

        assertNotNull(product);

        Product foundProduct = service.getProductById(product.getId());

        assertNotNull(foundProduct);
        assertEquals(1, foundProduct.getId());
        assertEquals("Product 1", foundProduct.getDescription());
        assertEquals(10.0, foundProduct.getAmount());
    }

    @Test
    void testGetProducts_HappyCase() {

        service.createProduct("Product 2", 20.0);

        List<Product> products = service.getProducts();

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(2, products.size());
    }

    @Test
    void testUpdateProduct_HappyCase() {

        Product updateProduct = new Product(product.getId(), "Product 1 Updated", 20.0);

        Product updatedProduct = service.updateProduct(product.getId(), updateProduct);

        assertNotNull(updatedProduct);
        assertEquals(updateProduct.getDescription(), updatedProduct.getDescription());
        assertEquals(updateProduct.getAmount(), updatedProduct.getAmount());
    }

    @Test
    void testUpdateProduct_NotFound() {

        Product updateProduct = new Product(999, "Product 999", 20.0);

        Product updatedProduct = service.updateProduct(999, updateProduct);

        assertNull(updatedProduct);
    }

    @Test
    void testDeleteProduct_HappyCase() {

        Product createdProduct = service.createProduct("Product 1", 10.0);
        assertNotNull(createdProduct);

        Product foundProduct = service.getProductById(createdProduct.getId());
        assertNotNull(foundProduct);
        assertEquals(createdProduct.getId(), foundProduct.getId());
        assertEquals(createdProduct.getDescription(), foundProduct.getDescription());
        assertEquals(createdProduct.getAmount(), foundProduct.getAmount());

        service.deleteProduct(createdProduct.getId());
        assertNull(service.getProductById(createdProduct.getId()));
    }
}
