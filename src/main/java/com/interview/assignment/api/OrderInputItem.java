package com.interview.assignment.api;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
class OrderInputItem {
    @NotNull
    private Long productId;
    @Min(1)
    @NotNull
    private Long quantity;
}
