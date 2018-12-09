package com.interview.assignment.api

import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class ControllerSpec extends Specification {
    @LocalServerPort
    def port
    def endpoint

    void initEndpoint(resource) {
        def versionedContent = "application/vnd.assignment.${resource}.v1+json"
        endpoint = new RESTClient("http://localhost:$port/${resource}s/", versionedContent)
        endpoint.encoder[versionedContent] = endpoint.encoder['application/json']
        endpoint.parser[versionedContent] = endpoint.parser['application/json']
    }
}
