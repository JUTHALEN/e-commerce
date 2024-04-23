package com.alende.judith.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alende.judith.model.Cart;
import com.alende.judith.model.Product;
import com.alende.judith.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    final Map<Long, Cart> carts = new ConcurrentHashMap<>();

    final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart createCart(List<Product> products) {

        final var cartId = cartIdGenerator.incrementAndGet();
        final var productMap = products.stream()
                .collect(Collectors.toConcurrentMap(Product::getId, Function.identity()));
        final var cart = new Cart(cartId, productMap, LocalDateTime.now().plusMinutes(10));
        carts.put(cartId, cart);
        return cart;
    }

    @Override
    public List<Cart> findAllCarts() {
        return new ArrayList<>(carts.values());
    }

    @Override
    public Cart getCartById(final Long id) {
        return carts.get(id);
    }

    @Override
    public Cart addProductToCart(final Long cartId, final Product product) {

        final var cart = carts.get(cartId);
        if (cart != null && cart.getExpiryTime().isAfter(LocalDateTime.now())) {
            cart.getProducts().put(product.getId(), product);
        }
        return cart;
    }

    @Override
    public Cart removeProductFromCart(final Long cartId, final int productId) {

        final var cart = carts.get(cartId);
        if (cart != null) {
            cart.getProducts().remove(productId);
        }
        return cart;
    }

    @Override
    public void deleteCart(final Long id) {
        carts.remove(id);
    }
}
