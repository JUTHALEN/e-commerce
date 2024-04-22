package com.alende.judith.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alende.judith.model.Product;
import com.alende.judith.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    final Map<Integer, Product> products = new ConcurrentHashMap<>();
    final AtomicInteger productIdGenerator = new AtomicInteger(0);


    @Override
    public Product createProduct(final String description, final double amount) {

        if (amount < 0) {
            logger.error("Amount cannot be negative {}", amount);
            throw new IllegalArgumentException("Product amount must not be negative");
        }
        final var productId = productIdGenerator.incrementAndGet();
        final var product = new Product(productId, description, amount);
        products.put(productId, product);
        return product;
    }

    @Override
    public Product getProductById(final int id) {
        if (!products.containsKey(id)) {
            logger.error("Product with id {} not found", id);
            return null;
        }
        return products.get(id);
    }

    @Override
    public List<Product> getProducts() {
        return List.copyOf(products.values());
    }


    @Override
    public Product updateProduct(final int id, final Product product) {
        final var existingProduct = products.get(id);
        if(existingProduct != null) {
            existingProduct.setDescription(product.getDescription());
            existingProduct.setAmount(product.getAmount());
            products.put(id, existingProduct);
            return existingProduct;
        }
        logger.error("Product with id {} not found", id);
        return null;
    }

    @Override
    public void deleteProduct(final int id) {
        products.remove(id);
    }
}

