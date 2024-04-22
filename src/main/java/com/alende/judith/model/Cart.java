package com.alende.judith.model;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    Long id;

    Map<Integer, Product> products;

    LocalDateTime expiryTime;
}
