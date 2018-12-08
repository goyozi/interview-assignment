package com.interview.assignment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItem {
    private Long productId;
    private BigDecimal price;
    private Long quantity;

    public OrderItem(Product product, Long quantity) {
        this.productId = product.getId();
        this.price = product.getPrice();
        this.quantity = quantity;
    }

    BigDecimal totalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
