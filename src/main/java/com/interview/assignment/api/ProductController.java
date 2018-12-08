package com.interview.assignment.api;

import com.interview.assignment.model.Product;
import com.interview.assignment.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/products", consumes = "application/vnd.assignment.product.v1+json")
public class ProductController {
    private final ProductRepository products;

    @PostMapping
    public void create(@Valid @RequestBody ProductInput input) {
        var product = new Product(input.getName(), input.getPrice());
        products.save(product);
    }
}
