package com.interview.assignment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.math.BigDecimal.ZERO;

@Data
@NoArgsConstructor
public class Order {
    private String buyer;
    private List<OrderItem> items;
    private LocalDateTime placedAt;

    public Order(String buyer, List<OrderItem> items) {
        this.buyer = buyer;
        this.items = items;
        this.placedAt = LocalDateTime.now();
    }

    public BigDecimal totalAmount() {
        return items.stream()
                .map(OrderItem::totalPrice)
                .reduce(ZERO, BigDecimal::add);
    }
}
