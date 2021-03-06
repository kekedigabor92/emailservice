package com.mycompany.emailservice.domain.model

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

class EmailDtoTest extends Specification {

    @Shared
    Validator validator

    def setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator()
    }

    def "valid emailDto"() {
        given:
        def email = new EmailDto(
                subject: "s",
                sender: "sender@test.com",
                recipients: ["recipient@test.com"],
                body: "body"
        )

        when:
        Set<ConstraintViolation<EmailDto>> violations = validator.validate(email)

        then:
        violations.isEmpty()
    }

    @Unroll
    def "invalid emailDto"() {
        given:
        def email = new EmailDto(
                sender: sender,
                recipients: recipients,
                carbonCopyRecipients: ccr,
                blindCarbonCopyRecipients: bccr,
                subject: subject,
                body: body
        )

        when:
        Set<ConstraintViolation<EmailDto>> violations = validator.validate(email)

        then:
        !violations.isEmpty()
        violations.asList().find { it.message == violationMessage }

        where:
        sender                  | recipients                | ccr                       | bccr                      | subject | body    | violationMessage
        null                    | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.SENDER_NULL_ERROR_MESSAGE
        "invalidEmail"          | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.SENDER_INVALID_ERROR_MESSAGE
        'x' * 312 + '@test.com' | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.SENDER_TOO_LONG_ERROR_MESSAGE
        "sender@test.com"       | null                      | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.RECIPIENTS_IS_MANDATORY_ERROR_MESSAGE
        "sender@test.com"       | []                        | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.RECIPIENTS_IS_MANDATORY_ERROR_MESSAGE
        "sender@test.com"       | [null]                    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE
        "sender@test.com"       | ["invalidEmail"]          | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.RECIPIENTS_CONTAINS_INVALID_EMAIL_ERROR_MESSAGE
        "sender@test.com"       | ['x' * 312 + '@test.com'] | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.RECIPIENT_TOO_LONG_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | [null]                    | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.CARBON_COPY_RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["invalidEmail"]          | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.CARBON_COPY_RECIPIENTS_CONTAINS_INVALID_EMAIL_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ['x' * 312 + '@test.com'] | ["bccr@test.com"]         | "s"     | "b"     | EmailDto.CARBON_COPY_RECIPIENT_TOO_LONG_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | [null]                    | "s"     | "b"     | EmailDto.BLIND_CARBON_COPY_RECIPIENTS_CONTAINS_NULL_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["invalidEmail"]          | "s"     | "b"     | EmailDto.BLIND_CARBON_COPY_RECIPIENTS_CONTAIN_INVALID_EMAIL_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ['x' * 312 + '@test.com'] | "s"     | "b"     | EmailDto.BLIND_CARBON_COPY_RECIPIENT_TOO_LONG_ERROR_MESSAGE
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | null    | "b"     | EmailDto.SUBJECT_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | ""      | "b"     | EmailDto.SUBJECT_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "     " | "b"     | EmailDto.SUBJECT_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | null    | EmailDto.BODY_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | ""      | EmailDto.BODY_NOT_BLANK_ERROR
        "sender@test.com"       | ["recipient@test.com"]    | ["ccr@test.com"]          | ["bccr@test.com"]         | "s"     | "     " | EmailDto.BODY_NOT_BLANK_ERROR
    }
}
