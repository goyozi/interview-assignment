package com.interview.assignment


import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class AssignmentApplicationSpec extends Specification {

    def "context loads"() {
        expect:
        true
    }
}
