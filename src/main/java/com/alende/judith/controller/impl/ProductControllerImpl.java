package com.alende.judith.controller.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alende.judith.controller.ProductController;
import com.alende.judith.dto.ProductRequest;
import com.alende.judith.dto.ProductResponse;
import com.alende.judith.model.Product;
import com.alende.judith.service.ProductService;

@RestController
public class ProductControllerImpl implements ProductController {

    @Autowired
    ProductService productService;

    @Override
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        if (request.getDescription() == null || request.getDescription().trim().isEmpty() || request.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        final var product = productService.createProduct(request.getDescription(), request.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponse(product));
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) {

        final var product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        ProductResponse response = new ProductResponse(product);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> responses = productService.getProducts().stream().map(ProductResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @RequestBody ProductRequest request) {

        if (request.getDescription() == null || request.getDescription().trim().isEmpty() || request.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Product product = new Product(id, request.getDescription(), request.getAmount());
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ProductResponse response = new ProductResponse(updatedProduct);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {

        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
