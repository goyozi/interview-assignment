package com.interview.assignment.api;

import com.interview.assignment.model.Order;
import com.interview.assignment.model.OrderItem;
import com.interview.assignment.persistence.OrderRepository;
import com.interview.assignment.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/orders")
public class OrderController {
    private static final String VERSIONED_CONTENT = "application/vnd.assignment.order.v1+json";

    private final ProductRepository products;
    private final OrderRepository orders;

    @PostMapping(consumes = VERSIONED_CONTENT)
    public void place(@Valid @RequestBody OrderInput input) {
        var order = new Order(input.getBuyer(), toOrderItems(input.getItems()));
        orders.save(order);
    }

    private List<OrderItem> toOrderItems(List<OrderInputItem> items) {
        return items.stream()
                .map(this::toOrderItem)
                .collect(toList());
    }

    private OrderItem toOrderItem(OrderInputItem item) {
        var product = products.findById(item.getProductId())
                .orElseThrow(() -> new MissingProductException(item.getProductId()));
        return new OrderItem(product, item.getQuantity());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingProductException.class)
    public void handleMissingProduct(MissingProductException exception) {
        log.warn("Order for non-existing product {}", exception.productId);
    }

    @GetMapping(produces = VERSIONED_CONTENT)
    public Set<OrderOutput> find(@RequestParam @DateTimeFormat(iso = DATE_TIME) LocalDateTime from,
                                 @RequestParam @DateTimeFormat(iso = DATE_TIME) LocalDateTime to) {
        return orders.findByPlacedAtBetween(from, to)
                .stream()
                .map(OrderOutput::new)
                .collect(toSet());
    }

    @RequiredArgsConstructor
    private static class MissingProductException extends RuntimeException {
        private final Long productId;
    }
}
