package com.interview.assignment.api;

import com.interview.assignment.model.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
class ProductOutput {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    ProductOutput(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }
}
