package com.interview.assignment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    private String buyer;
    @OneToMany(cascade = ALL, fetch = EAGER)
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
