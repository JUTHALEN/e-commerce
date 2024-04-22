package com.alende.judith.service;

import java.util.List;

import com.alende.judith.model.Cart;
import com.alende.judith.model.Product;

public interface CartService {

    Cart createCart(List<Product> products);

    List<Cart> findAllCarts();

    Cart getCartById(Long id);

    Cart addProductToCart(Long cartId, Product product);

    Cart removeProductFromCart(Long cartId, int productId);

    void deleteCart(Long id);
}
