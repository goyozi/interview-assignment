package com.interview.assignment.api;

import com.interview.assignment.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
class OrderOutputItem {
    private Long productId;
    private BigDecimal price;
    private Long quantity;

    OrderOutputItem(OrderItem item) {
        this.productId = item.getProductId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
    }
}
