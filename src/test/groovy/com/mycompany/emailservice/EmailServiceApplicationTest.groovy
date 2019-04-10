package com.mycompany.emailservice

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = EmailServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class EmailServiceApplicationTest extends Specification {

    def "context loads"() {
        when: "spring app starts"

        then:
        noExceptionThrown()
    }
}
