package com.interview.assignment.api

import com.interview.assignment.model.Product
import com.interview.assignment.persistence.ProductRepository
import groovyx.net.http.HttpResponseException
import org.springframework.beans.factory.annotation.Autowired

class ProductControllerSpec extends ControllerSpec {

    @Autowired
    ProductRepository products

    void setup() {
        initEndpoint('product')
    }

    void cleanup() {
        products.deleteAll()
    }

    def "successful product creation"() {
        when:
        def response = endpoint.post(body: [name: 'some-product', price: 1.23])

        then:
        response.status == 200

        and:
        onlyProduct.id != null
        onlyProduct.name == 'some-product'
        onlyProduct.price == 1.23
    }

    def getOnlyProduct() {
        def products = products.findAll()
        assert products.size() == 1
        return products[0]
    }

    def "unsuccessful product creation"() {
        when:
        endpoint.post(body: [name: name, price: price])

        then:
        def e = thrown HttpResponseException
        e.response.status == 400

        and:
        products.count() == 0

        where:
        name      | price
        null      | 1.23
        ''        | 1.23
        ' '       | 1.23
        'correct' | -1
    }

    def "product listing - no products"() {
        when:
        def response = endpoint.get([:])

        then:
        response.status == 200

        and:
        response.data.size() == 0
    }

    def "product listing - with products"() {
        given:
        def product1 = new Product("doesn't matter", 1.23)
        def product2 = new Product("still doesn't", 2.34)
        products.save(product1)
        products.save(product2)

        when:
        def response = endpoint.get([:])

        then:
        response.status == 200

        and:
        response.data.size() == 2
        response.data.any { it.id != null && it.name == "doesn't matter" && it.price == 1.23 }
        response.data.any { it.id != null && it.name == "still doesn't" && it.price == 2.34 }
    }

    def "successful product update"() {
        given:
        def product = products.save(new Product('old-name', 1.23))

        when:
        def response = endpoint.put(path: "$product.id", body: [name: 'new-name', price: 2.34])

        then:
        response.status == 200

        and:
        def updated = products.findById(product.id).get()
        updated.name == 'new-name'
        updated.price == 2.34
    }

    def "unsuccessful product update - no product"() {
        when:
        endpoint.put(path: '123', body: [name: 'not-used', price: 1.23])

        then:
        def e = thrown(HttpResponseException)
        e.response.status == 404
    }

    def "unsuccessful product update - invalid parameters"() {
        given:
        def product = products.save(new Product('old-name', 1.23))

        when:
        endpoint.put(path: "$product.id", body: [name: name, price: price])

        then:
        def e = thrown HttpResponseException
        e.response.status == 400

        and:
        def updated = products.findById(product.id).get()
        updated.name == 'old-name'
        updated.price == 1.23

        where:
        name       | price
        null       | 2.34
        ''         | 2.34
        ' '        | 2.34
        'new-name' | -1
    }
}
