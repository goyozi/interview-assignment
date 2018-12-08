package com.interview.assignment.api;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
class OrderInput {
    @Email
    @NotBlank
    private String buyer;
    @Valid
    @NotEmpty
    private List<OrderInputItem> items;
}
