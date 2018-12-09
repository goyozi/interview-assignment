package com.interview.assignment.api;


import com.interview.assignment.model.Order;
import com.interview.assignment.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
class OrderOutput {
    private Long id;
    private String buyer;
    private List<OrderOutputItem> items;
    private BigDecimal totalAmount;
    private LocalDateTime placedAt;

    OrderOutput(Order order) {
        this.id = order.getId();
        this.buyer = order.getBuyer();
        this.items = toOutputItems(order.getItems());
        this.totalAmount = order.totalAmount();
        this.placedAt = order.getPlacedAt();
    }

    private List<OrderOutputItem> toOutputItems(List<OrderItem> items) {
        return items.stream()
                .map(OrderOutputItem::new)
                .collect(toList());
    }
}
