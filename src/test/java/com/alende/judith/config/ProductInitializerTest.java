package com.alende.judith.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.alende.judith.service.ProductService;

@SpringBootTest
@ActiveProfiles("dev")
public class ProductInitializerTest {

    @InjectMocks
    ProductInitializer productInitializer;

    ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = mock(ProductService.class);

        productInitializer = new ProductInitializer();
        productInitializer.productService = productService;
    }

    @Test
    public void testProductInitialization() {

        productInitializer.init();

        verify(productService, times(1)).createProduct("Product 1", 100);
        verify(productService, times(1)).createProduct("Product 2", 200);
        verify(productService, times(1)).createProduct("Product 3", 300);
        verifyNoMoreInteractions(productService);
    }
}