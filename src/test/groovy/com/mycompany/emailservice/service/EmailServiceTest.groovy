package com.mycompany.emailservice.service

import com.mycompany.emailservice.client.EmailClient
import com.mycompany.emailservice.domain.model.EmailDto
import com.mycompany.emailservice.domain.validator.EmailValidator
import com.mycompany.emailservice.exception.EmailServiceException
import com.mycompany.emailservice.mapper.EmailMapper
import com.mycompany.emailservice.service.impl.EmailServiceImpl
import com.sendgrid.Mail
import com.sendgrid.Response
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification

class EmailServiceTest extends Specification {

    @Shared
    EmailValidator emailValidator

    @Shared
    EmailMapper emailMapper

    @Shared
    EmailClient emailClient

    @Shared
    EmailService emailService

    def setup() {
        emailValidator = Mock(EmailValidator)
        emailMapper = Mock(EmailMapper)
        emailClient = Mock(EmailClient)

        emailService = new EmailServiceImpl(emailValidator, emailMapper, emailClient)
    }

    def "sendEmail - positive"() {
        given:
        def emailDto = new EmailDto()
        def mail = new Mail()
        def response = new Response(HttpStatus.ACCEPTED.value(), null, null)

        when:
        def actualEmailDto = emailService.sendEmail(emailDto)

        then:
        noExceptionThrown()

        and:
        actualEmailDto == emailDto

        and:
        1 * emailValidator.validateEmail(emailDto)
        1 * emailMapper.toEmail(emailDto) >> mail
        1 * emailClient.send(mail) >> response
    }

    def "sendEmail - negative - NOT accepted status is returned"() {
        given:
        def emailDto = new EmailDto()
        def mail = new Mail()

        when:
        emailService.sendEmail(emailDto)

        then:
        def e = thrown(EmailServiceException)
        e.errorDetailsDto.httpStatus == HttpStatus.INTERNAL_SERVER_ERROR

        and:
        1 * emailValidator.validateEmail(emailDto)
        1 * emailMapper.toEmail(emailDto) >> mail
        1 * emailClient.send(mail) >> response

        where:
        response << [null,
                     new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null)
        ]
    }

    def "sendEmail - negative - exception is thrown"() {
        given:
        def emailDto = new EmailDto()
        def mail = new Mail()

        when:
        emailService.sendEmail(emailDto)

        then:
        def e = thrown(EmailServiceException)
        e.errorDetailsDto.httpStatus == HttpStatus.BAD_REQUEST

        and:
        1 * emailValidator.validateEmail(emailDto)
        1 * emailMapper.toEmail(emailDto) >> mail
        1 * emailClient.send(mail) >> { throw new IOException() }
    }
}
