package com.alende.judith.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.alende.judith.model.Cart;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2265008860347503245L;

    Long id;

    private Map<Integer, ProductResponse> products;

    private LocalDateTime expiryTime;

    public CartResponse(Cart cart) {

        this.id = cart.getId();
        this.products = cart.getProducts().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> new ProductResponse(entry.getValue())));
        this.expiryTime = cart.getExpiryTime();
    }
}
