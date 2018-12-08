package com.interview.assignment.persistence;

import com.interview.assignment.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
