package com.alende.judith.service;

import java.util.List;

import com.alende.judith.model.Product;

public interface ProductService {

    Product createProduct(String description, double amount);

    Product getProductById(int id);

    List<Product> getProducts();

    Product updateProduct(int id, Product product);

    void deleteProduct(int id);
}
