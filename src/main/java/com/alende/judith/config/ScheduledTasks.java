package com.alende.judith.config;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alende.judith.service.CartService;

@Component
public class ScheduledTasks {

    private final int CLEANUP_INTERVAL = 600000;

    @Value("${cart.expiry.time:10}")
    int cartExpiryTime;

    CartService cartService;

    @Scheduled(fixedRate = CLEANUP_INTERVAL)
    public void cleanupExpiredCarts() {

        final var currentTime = LocalDateTime.now();
        cartService.findAllCarts().forEach(cart -> {
            if(ChronoUnit.MINUTES.between(cart.getExpiryTime(), currentTime) >= cartExpiryTime) {
                cartService.deleteCart(cart.getId());
            }
        });
    }
}
