package com.mycompany.emailservice.configuration.interceptor

import spock.lang.Specification

class RequestContextTest extends Specification {

    def "getRequestId - instance id is different for different instances"() {
        given:
        def requestContextOne = new RequestContext()
        def requestContextTwo = new RequestContext()

        when:
        def firstId = requestContextOne.getRequestId();
        def secondId = requestContextTwo.getRequestId();

        then:
        firstId
        secondId
        firstId != secondId
    }
}
