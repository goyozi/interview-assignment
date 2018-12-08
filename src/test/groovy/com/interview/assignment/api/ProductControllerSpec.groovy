package com.interview.assignment.api

import com.interview.assignment.persistence.ProductRepository
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
    def http

    void setup() {
        http = new RESTClient("http://localhost:$port")
        http.encoder[VERSIONED_CONTENT] = http.encoder['application/json']
    }

    def "product creation"() {
        when:
        def response = http.post([
                path              : '/products',
                body              : [name: 'some-product', price: 1.23],
                requestContentType: VERSIONED_CONTENT
        ])

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
}
