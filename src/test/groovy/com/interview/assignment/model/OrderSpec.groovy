package com.interview.assignment.model

import spock.lang.Specification

class OrderSpec extends Specification {
    static final product1 = new Product("doesn't matter", 1.23)
    static final product2 = new Product("doesn't matter", 2.34)

    def "total amount"() {
        given: "an order with two items"
        def item1 = new OrderItem(product1, 2)
        def item2 = new OrderItem(product2, 3)
        def order = new Order("doesnt@matter.com", [item1, item2])

        expect: "total amount to be calculated correctly"
        order.totalAmount() == 9.48

        when: "the price of the ordered products changes"
        product1.price = 3.45
        product2.price = 4.56

        then: "total amount remains the same"
        order.totalAmount() == 9.48
    }
}
