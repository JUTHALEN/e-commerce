package com.alende.judith.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alende.judith.dto.ProductRequest;
import com.alende.judith.dto.ProductResponse;

public interface ProductController {

    @PostMapping("/api/product")
    ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request);

    @PostMapping("/api/product/{id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable int id);

    @PostMapping("/api/products")
    ResponseEntity<List<ProductResponse>> getAllProducts();

    @PostMapping("/api/product/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @RequestBody ProductRequest request);

    @PostMapping("/api/product/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable int id);
}
