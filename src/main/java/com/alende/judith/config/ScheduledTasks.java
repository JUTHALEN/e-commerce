package com.alende.judith.config;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alende.judith.service.CartService;

@Component
public class ScheduledTasks {

    CartService cartService;

    @Scheduled(fixedRate = 600000)
    public void cleanupExpiredCarts() {

        LocalDateTime currentTime = LocalDateTime.now();
        cartService.findAllCarts().forEach(cart -> {
            if(ChronoUnit.MINUTES.between(cart.getExpiryTime(), currentTime) >= 10) {
                cartService.deleteCart(cart.getId());
            }
        });
    }
}
