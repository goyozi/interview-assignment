package com.interview.assignment.api;

import com.interview.assignment.model.Product;
import com.interview.assignment.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

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

    @GetMapping(produces = VERSIONED_CONTENT)
    public Set<ProductOutput> findAll() {
        return stream(products.findAll().spliterator(), false)
                .map(ProductOutput::new)
                .collect(toSet());
    }

    @Transactional
    @PutMapping(path = "/{productId}", consumes = VERSIONED_CONTENT)
    public ResponseEntity update(@PathVariable Long productId, @Valid @RequestBody ProductInput input) {
        var product = products.findById(productId);
        product.ifPresent(p -> update(p, input));
        return product.map(p -> ok().build()).orElse(notFound().build());
    }

    private void update(Product product, ProductInput input) {
        product.setName(input.getName());
        product.setPrice(input.getPrice());
    }
}
