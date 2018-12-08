package com.interview.assignment.api

import com.interview.assignment.model.Product
import com.interview.assignment.persistence.ProductRepository
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductControllerSpec extends Specification {
    static final VERSIONED_CONTENT = 'application/vnd.assignment.product.v1+json'

    @Autowired
    ProductRepository products

    @LocalServerPort
    def port
    def endpoint

    void setup() {
        endpoint = new RESTClient("http://localhost:$port/products", VERSIONED_CONTENT)
        endpoint.encoder[VERSIONED_CONTENT] = endpoint.encoder['application/json']
        endpoint.parser[VERSIONED_CONTENT] = endpoint.parser['application/json']
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
}
