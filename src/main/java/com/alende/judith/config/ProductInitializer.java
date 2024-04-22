package com.alende.judith.config;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.alende.judith.service.ProductService;

@Configuration
@Profile("dev")
public class ProductInitializer {

    final Logger logger = LoggerFactory.getLogger(ProductInitializer.class);

    @Autowired
    ProductService productService;

    @PostConstruct
    public void init() {

        logger.info("Creating initial products");
        productService.createProduct("Product 1", 100);
        productService.createProduct("Product 2", 200);
        productService.createProduct("Product 3", 300);
    }
}
