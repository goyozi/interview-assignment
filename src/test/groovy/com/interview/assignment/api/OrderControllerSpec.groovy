package com.interview.assignment.api

import com.interview.assignment.model.Order
import com.interview.assignment.model.OrderItem
import com.interview.assignment.model.Product
import com.interview.assignment.persistence.OrderRepository
import com.interview.assignment.persistence.ProductRepository
import groovyx.net.http.HttpResponseException
import org.springframework.beans.factory.annotation.Autowired

import static java.time.LocalDateTime.now
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

class OrderControllerSpec extends ControllerSpec {

    @Autowired
    ProductRepository products
    @Autowired
    OrderRepository orders

    void setup() {
        initEndpoint('order')
    }

    void cleanup() {
        products.deleteAll()
        orders.deleteAll()
    }

    def "successful order placement"() {
        given:
        def product1 = products.save(new Product('product1', 1.23))
        def product2 = products.save(new Product('product2', 2.34))

        when:
        def response = endpoint.post(body: [
                buyer: 'some@email.com',
                items: [[productId: product1.id, quantity: 2],
                        [productId: product2.id, quantity: 3]]
        ])

        then:
        response.status == 200

        and:
        onlyOrder.id != null
        onlyOrder.buyer == 'some@email.com'
        onlyOrder.items.size() == 2

        onlyOrder.items[0].id != null
        onlyOrder.items[0].productId == product1.id
        onlyOrder.items[0].price == 1.23
        onlyOrder.items[0].quantity == 2

        onlyOrder.items[1].id != null
        onlyOrder.items[1].productId == product2.id
        onlyOrder.items[1].price == 2.34
        onlyOrder.items[1].quantity == 3
    }

    def getOnlyOrder() {
        def orders = orders.findAll()
        assert orders.size() == 1
        return orders[0]
    }

    def "unsuccessful order placement - missing product"() {
        when:
        endpoint.post(body: [
                buyer: 'some@email.com',
                items: [[productId: -1, quantity: 1]]
        ])

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 400
    }

    def "unsuccessful order placement - invalid request"() {
        when:
        endpoint.post(body: [
                buyer: email,
                items: items
        ])

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 400

        where:
        email             | items
        null              | [[productId: 1, quantity: 1]]
        ''                | [[productId: 1, quantity: 1]]
        'not-email'       | [[productId: 1, quantity: 1]]
        'email@email.com' | [[productId: null, quantity: 1]]
        'email@email.com' | [[productId: 1, quantity: null]]
        'email@email.com' | [[productId: 1, quantity: 0]]
        'email@email.com' | []
    }

    def "order search - no orders found"() {
        when:
        def response = endpoint.get(query: [from: now().toString(), to: now().toString()])

        then:
        response.status == 200

        and:
        response.data == []
    }

    def "order search - order found"() {
        given:
        def product1 = products.save(new Product('product1', 1.23))
        def product2 = products.save(new Product('product2', 2.34))

        def item1 = new OrderItem(product1, 2)
        def item2 = new OrderItem(product2, 3)
        def order = orders.save(new Order('some@email.com', [item1, item2]))

        when:
        def response = endpoint.get(query: [from: order.placedAt.minusSeconds(1).toString(),
                                            to  : order.placedAt.plusSeconds(1).toString()])

        then:
        response.status == 200

        and:
        response.data == [
                [
                        id         : order.id,
                        buyer      : 'some@email.com',
                        items      : [
                                [productId: product1.id, price: 1.23, quantity: 2],
                                [productId: product2.id, price: 2.34, quantity: 3]
                        ],
                        totalAmount: 9.48,
                        // gotta use actually stored time, because h2 is not 100% precise
                        placedAt   : onlyOrder.placedAt.format(ISO_LOCAL_DATE_TIME)
                ]
        ]
    }

    def "order search - invalid parameters"() {
        when:
        endpoint.get(query: parameters)

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 400

        where:
        parameters << [
                [to: now().toString()],
                [from: now().toString()],
                [from: 'incorrect', to: now().toString()],
                [from: now().toString(), to: 'incorrect']
        ]
    }
}
