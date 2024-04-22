package com.alende.judith.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.alende.judith.model.Cart;
import com.alende.judith.model.Product;

class CartResponseTest {

    @Test
    public void testCartResponseConstructor() {

        Long cartId = 1L;
        LocalDateTime expiryTime = LocalDateTime.now().plusDays(1);
        Map<Integer, Product> productMap = new HashMap<>();
        productMap.put(1, new Product(1, "Product 1", 10.0));
        Cart cart = new Cart(cartId, productMap, expiryTime);

        CartResponse cartResponse = new CartResponse(cart);

        assertEquals(cartId, cartResponse.getId());
        assertEquals(expiryTime, cartResponse.getExpiryTime());
        assertNotNull(cartResponse.getProducts());
        assertFalse(cartResponse.getProducts().isEmpty());
        assertEquals(1, cartResponse.getProducts().size());
        assertEquals("Product 1", cartResponse.getProducts().get(1).getDescription());
        assertEquals(10.0, cartResponse.getProducts().get(1).getAmount());
    }
}
