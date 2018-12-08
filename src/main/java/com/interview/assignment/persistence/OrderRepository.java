package com.interview.assignment.persistence;

import com.interview.assignment.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
