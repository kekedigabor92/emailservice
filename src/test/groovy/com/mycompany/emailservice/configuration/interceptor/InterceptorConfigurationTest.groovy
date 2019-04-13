package com.mycompany.emailservice.configuration.interceptor

import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import spock.lang.Shared
import spock.lang.Specification

class InterceptorConfigurationTest extends Specification {

    @Shared
    MdcHandlerInterceptor mdcHandlerInterceptor

    @Shared
    InterceptorConfiguration interceptorConfiguration

    def setup() {
        mdcHandlerInterceptor = Mock(MdcHandlerInterceptor)

        interceptorConfiguration = new InterceptorConfiguration(mdcHandlerInterceptor)
    }

    def "addInterceptors"() {
        given:
        def interceptorRegistry = Mock(InterceptorRegistry)

        when:
        interceptorConfiguration.addInterceptors(interceptorRegistry)

        then:
        1 * interceptorRegistry.addInterceptor(mdcHandlerInterceptor)
    }
}
