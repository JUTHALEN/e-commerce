package com.alende.judith.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alende.judith.dto.ProductRequest;
import com.alende.judith.dto.ProductResponse;

public interface ProductController {

    @PostMapping("/api/product")
    ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request);

    @GetMapping("/api/product/{id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable int id);

    @GetMapping("/api/products")
    ResponseEntity<List<ProductResponse>> getAllProducts();

    @PutMapping("/api/product/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @RequestBody ProductRequest request);

    @DeleteMapping("/api/product/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable int id);
}
