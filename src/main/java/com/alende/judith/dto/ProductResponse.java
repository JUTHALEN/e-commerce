package com.alende.judith.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.alende.judith.model.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -6053467124601868172L;

    int id;

    String description;

    double amount;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.description = product.getDescription();
        this.amount = product.getAmount();
    }
}