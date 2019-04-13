package com.mycompany.emailservice.integration

import com.mycompany.emailservice.EmailServiceApplication
import com.mycompany.emailservice.domain.model.EmailDto
import com.mycompany.emailservice.domain.model.ErrorDetailsDto
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest(classes = EmailServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmailServiceApplicationIT extends Specification {

    @LocalServerPort
    int port

    @Shared
    TestRestTemplate restTemplate

    @Shared
    HttpHeaders headers

    def setup() {
        restTemplate = new TestRestTemplate()
        headers = new HttpHeaders()
    }

    def "valid request succeeds"() {
        given:
        def email = new EmailDto(
                sender: "sender@test.com",
                recipients: ["recipient@test.com"],
                carbonCopyRecipients: ["carboncopyrecipient@test.com"],
                blindCarbonCopyRecipients: ["blindcarboncopyrecipient@test.com"],
                subject: "subject",
                body: "body")

        HttpEntity<EmailDto> entity = new HttpEntity<>(email, headers)

        when:
        ResponseEntity<EmailDto> response = restTemplate
                .exchange(createURLWithPort("/emails"), HttpMethod.POST, entity, EmailDto.class)
        then:
        response.statusCode == HttpStatus.ACCEPTED
    }

    @Unroll
    def "wrong input parameters result in bad request "() {
        given:
        def email = new EmailDto(
                sender: sender,
                recipients: recipients,
                carbonCopyRecipients: ccr,
                blindCarbonCopyRecipients: bccr,
                subject: subject,
                body: body
        )

        HttpEntity<EmailDto> entity = new HttpEntity<>(email, headers)

        when:
        ResponseEntity<ErrorDetailsDto> response = restTemplate
                .exchange(createURLWithPort("/emails"), HttpMethod.POST, entity, ErrorDetailsDto.class)
        then:
        response.statusCode == HttpStatus.BAD_REQUEST
        response.body.message == violationMessage

        where:
        sender                  | recipients                | ccr                       | bccr                      | subject | body | violationMessage
        null                    | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.SENDER_NULL_ERROR_MESSAGE
        "invalidEmail"          | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.SENDER_INVALID_ERROR_MESSAGE
        'x' * 312 + '@test.com' | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.SENDER_TOO_LONG_ERROR_MESSAGE
        "sender@test.com"       | null                      | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.RECIPIENTS_IS_MANDATORY_ERROR_MESSAGE
        "sender@test.com"       | []                        | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.RECIPIENTS_IS_MANDATORY_ERROR_MESSAGE
        "sender@test.com"       | [null]                    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE
        "sender@test.com"       | ["invalidEmail"]          | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.RECIPIENTS_CONTAINS_INVALID_EMAIL_ERROR_MESSAGE
        "sender@test.com"       | ['x' * 312 + '@test.com'] | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.RECIPIENT_TOO_LONG_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | [null]                    | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.CARBON_COPY_RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["invalidEmail"]          | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.CARBON_COPY_RECIPIENTS_CONTAINS_INVALID_EMAIL_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ['x' * 312 + '@test.com'] | ["bccr@test.com"]         | "s"     | "b"  | EmailDto.CARBON_COPY_RECIPIENT_TOO_LONG_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | [null]                    | "s"     | "b"  | EmailDto.BLIND_CARBON_COPY_RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["invalidEmail"]          | "s"     | "b"  | EmailDto.BLIND_CARBON_COPY_RECIPIENTS_CONTAIN_INVALID_EMAIL_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ['x' * 312 + '@test.com'] | "s"     | "b"  | EmailDto.BLIND_CARBON_COPY_RECIPIENT_TOO_LONG_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | null    | "b"  | EmailDto.SUBJECT_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | ""      | "b"  | EmailDto.SUBJECT_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "     " | "b"  | EmailDto.SUBJECT_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | null | EmailDto.BODY_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | ""   | EmailDto.BODY_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "  " | EmailDto.BODY_NOT_BLANK_ERROR

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/emailservice/api/v1" + uri
    }
}
