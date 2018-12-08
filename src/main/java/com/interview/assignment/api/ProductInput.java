package com.interview.assignment.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
class ProductInput {
    private String name;
    private BigDecimal price;
}
