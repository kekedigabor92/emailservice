package com.mycompany.emailservice.controller

import com.mycompany.emailservice.domain.model.EmailDto
import com.mycompany.emailservice.service.EmailService
import spock.lang.Shared
import spock.lang.Specification

class EmailControllerTest extends Specification {

    @Shared
    EmailService emailService

    @Shared
    EmailController emailController

    def setup() {
        emailService = Mock(EmailService)
        emailController = new EmailController(emailService)
    }

    def "sendEmail"() {
        given:
        def emailDto = new EmailDto()

        when:
        emailController.sendEmail(emailDto)

        then:
        noExceptionThrown()
        and:
        1 * emailService.sendEmail(emailDto)
    }
}
