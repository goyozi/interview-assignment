package com.interview.assignment.api;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
class ProductInput {
    @NotBlank
    private String name;
    @Min(0)
    private BigDecimal price;
}
