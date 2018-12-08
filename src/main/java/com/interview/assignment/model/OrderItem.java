package com.interview.assignment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id;
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
