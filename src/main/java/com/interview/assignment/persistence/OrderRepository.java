package com.interview.assignment.persistence;

import com.interview.assignment.model.Order;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Collection<Order> findByPlacedAtBetween(LocalDateTime from, LocalDateTime to);
}
