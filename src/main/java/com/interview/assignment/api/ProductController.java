package com.interview.assignment.api;

import com.interview.assignment.model.Product;
import com.interview.assignment.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/products")
public class ProductController {
    private static final String VERSIONED_CONTENT = "application/vnd.assignment.product.v1+json";

    private final ProductRepository products;

    @PostMapping(consumes = VERSIONED_CONTENT)
    public void create(@Valid @RequestBody ProductInput input) {
        var product = new Product(input.getName(), input.getPrice());
        products.save(product);
    }

    @ResponseBody
    @GetMapping(produces = VERSIONED_CONTENT)
    public Set<ProductOutput> findAll() {
        return stream(products.findAll().spliterator(), false)
                .map(ProductOutput::new)
                .collect(toSet());
    }
}
