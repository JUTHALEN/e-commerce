package com.alende.judith.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alende.judith.model.Cart;
import com.alende.judith.model.Product;

class CartServiceImplTest {

    CartServiceImpl service;

    Product product;

    List<Product> products;

    Cart cart;

    Long cartId;

    @BeforeEach
    void setUp() {

        service = new CartServiceImpl();
        product = new Product(1, "Product 1", 10.0);
        products = List.of(product);
    }

    @Test
    void testCreateCart() {

        cart = service.createCart(products);
        assertNotNull(cart);
        assertEquals(1, cart.getProducts().size());
        assertTrue(cart.getProducts().containsKey(product.getId()));
    }

    @Test
    void testFindAllCarts() {

        cart = service.createCart(products);
        assertNotNull(cart);
        assertEquals(1, cart.getProducts().size());
        assertTrue(cart.getProducts().containsKey(product.getId()));

        List<Cart> carts = service.findAllCarts();
        assertNotNull(carts);
        assertEquals(1, carts.size());
        assertTrue(carts.contains(cart));
    }

    @Test
    void testAddProductToCart() {

        cart = service.createCart(products);
        assertNotNull(cart);
        assertEquals(1, cart.getProducts().size());
        assertTrue(cart.getProducts().containsKey(product.getId()));

        Product product2 = new Product(2, "Product 2", 20.0);
        cart = service.addProductToCart(cart.getId(), product2);
        assertNotNull(cart);
        assertEquals(2, cart.getProducts().size());
        assertTrue(cart.getProducts().containsKey(product2.getId()));
    }

    @Test
    public void addProductToCart_AddProduct_WhenCartIsNotExpired() {

        cart = new Cart(1L, new ConcurrentHashMap<>(), LocalDateTime.now().plusMinutes(10));
        cartId = service.createCart(Collections.singletonList(product)).getId();
        Product newProduct = new Product(2, "New Product", 20.0);

        Cart updatedCart = service.addProductToCart(cartId, newProduct);

        assertNotNull(updatedCart);
        assertTrue(updatedCart.getProducts().containsKey(newProduct.getId()));
        assertEquals(newProduct, updatedCart.getProducts().get(newProduct.getId()));
    }

    @Test
    public void addProductToCart_ShouldNotAddProduct_WhenCartIsExpired() {

        cart = new Cart(1L, new ConcurrentHashMap<>(), LocalDateTime.now().plusMinutes(10));
        cartId = service.createCart(Collections.singletonList(product)).getId();
        Product newProduct = new Product(2, "New Product", 20.0);

        Cart expiredCart = new Cart(cartId, new ConcurrentHashMap<>(), LocalDateTime.now().minusMinutes(1));
        service.carts.put(cartId, expiredCart);

        // Act
        Cart updatedCart = service.addProductToCart(cartId, newProduct);

        // Assert
        assertNotNull(updatedCart);
        assertFalse(updatedCart.getProducts().containsKey(newProduct.getId()));
    }

    @Test
    public void testGetCartById_ReturnCart_WhenExists() {

        Cart createdCart = service.createCart(List.of(product));

        Cart foundCart = service.getCartById(createdCart.getId());

        assertNotNull(foundCart);
        assertEquals(createdCart.getId(), foundCart.getId());
    }

    @Test
    public void getCartById_ReturnNull_WhenNotExists() {

        Cart foundCart = service.getCartById(999L);

        assertNull(foundCart);
    }

    @Test
    public void testRemoveProductFromCart() {

        cart = service.createCart(products);
        assertNotNull(cart);
        assertEquals(1, cart.getProducts().size());
        assertTrue(cart.getProducts().containsKey(product.getId()));

        cart = service.removeProductFromCart(cart.getId(), product.getId());
        assertNotNull(cart);
        assertEquals(0, cart.getProducts().size());
        assertFalse(cart.getProducts().containsKey(product.getId()));
    }

    @Test
    public void testDeleteCart() {

        cart = service.createCart(products);
        assertNotNull(cart);
        assertEquals(1, cart.getProducts().size());
        assertTrue(cart.getProducts().containsKey(product.getId()));

        service.deleteCart(cart.getId());
        assertNull(service.getCartById(cart.getId()));
    }
}
