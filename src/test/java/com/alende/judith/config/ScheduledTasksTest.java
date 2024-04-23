package com.alende.judith.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.alende.judith.model.Cart;
import com.alende.judith.service.CartService;

class ScheduledTasksTest {

    @InjectMocks
    ScheduledTasks scheduledTasks;

    CartService cartService;

    @BeforeEach
    public void setUp() {

        cartService = mock(CartService.class);

        scheduledTasks = new ScheduledTasks();
        scheduledTasks.cartService = cartService;
    }

    @Test
    public void cleanupExpiredCarts_ShouldDeleteExpiredCarts() {

        Cart cart1 = new Cart(1L, null, LocalDateTime.now().minusMinutes(11));
        Cart cart2 = new Cart(2L, null, LocalDateTime.now().plusMinutes(5));
        when(cartService.findAllCarts()).thenReturn(Arrays.asList(cart1, cart2));

        scheduledTasks.cleanupExpiredCarts();

        verify(cartService).deleteCart(cart1.getId());
        verify(cartService, never()).deleteCart(cart2.getId());
    }
}
