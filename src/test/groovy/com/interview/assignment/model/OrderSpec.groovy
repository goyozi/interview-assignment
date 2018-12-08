package com.interview.assignment.model

import spock.lang.Specification

class OrderSpec extends Specification {
    static final product1 = new Product("doesn't matter", 1.23)
    static final product2 = new Product("doesn't matter", 2.34)

    def "total amount"() {
        given:
        def item1 = new OrderItem(product1, 2)
        def item2 = new OrderItem(product2, 3)
        def order = new Order("doesnt@matter.com", [item1, item2])

        expect:
        order.totalAmount() == 9.48
    }
}
