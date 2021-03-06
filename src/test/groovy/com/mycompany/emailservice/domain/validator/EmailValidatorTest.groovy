package com.mycompany.emailservice.domain.validator

import com.mycompany.emailservice.domain.model.EmailDto
import com.mycompany.emailservice.exception.EmailServiceException
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class EmailValidatorTest extends Specification {

    @Shared
    EmailValidator emailValidator

    def setup() {
        emailValidator = new EmailValidator()
    }

    @Unroll
    def "validateEmail - positive"() {
        given:
        def email = new EmailDto(recipients: ["recipient@test.com"],
                carbonCopyRecipients: ccr,
                blindCarbonCopyRecipients: bccr
        )
        when:
        emailValidator.validateEmail(email)

        then:
        noExceptionThrown()

        where:
        ccr              | bccr
        null             | null
        null             | []
        []               | null
        []               | []
        ["ccr@test.com"] | null
        ["ccr@test.com"] | []
        null             | ["bccr@test.com"]
        []               | ["bccr@test.com"]
        ["ccr@test.com"] | ["bccr@test.com"]
    }

    def "validateEmail - total recipients limit exceeded"() {
        given:
        def recipients = []
        333.times { recipients.add("test@test.com") }
        def carbonCopyRecipients = []
        334.times { carbonCopyRecipients.add("test@test.com") }
        def blindCarbonCopyRecipients = []
        334.times { blindCarbonCopyRecipients.add("test@test.com") }

        def email = new EmailDto(recipients: recipients,
                carbonCopyRecipients: carbonCopyRecipients,
                blindCarbonCopyRecipients: blindCarbonCopyRecipients
        )

        when:
        emailValidator.validateEmail(email)

        then:
        def e = thrown(EmailServiceException)
        e.errorDetailsDto.httpStatus == HttpStatus.BAD_REQUEST
        e.errorDetailsDto.message == EmailValidator.TOTAL_RECIPIENTS_EXCEEDED + EmailValidator.TOTAL_RECIPIENTS_LIMIT
    }

    @Unroll
    def "validateEmail - duplicates found"() {
        given:
        def email = new EmailDto(recipients: recipients,
                carbonCopyRecipients: ccr,
                blindCarbonCopyRecipients: bccr
        )

        when:
        emailValidator.validateEmail(email)

        then:
        def e = thrown(EmailServiceException)
        e.errorDetailsDto.httpStatus == HttpStatus.BAD_REQUEST
        e.errorDetailsDto.errorVariable
        !e.errorDetailsDto.errorVariable.isEmpty()
        Set<String> duplicates = e.errorDetailsDto.errorVariable.get("Duplicates")
        duplicates.size() == expectedDuplicatesSize

        where:
        recipients                                   | ccr                               | bccr                                    | expectedDuplicatesSize
        ["recipient@test.com", "recipient@test.com"] | []                                | []                                      | 1
        ["recipient@test.com"]                       | ["recipient@test.com"]            | []                                      | 1
        ["recipient@test.com"]                       | ["ccr@test.com"]                  | ["recipient@test.com"]                  | 1
        ["recipient@test.com"]                       | ["ccr@test.com"]                  | ["ccr@test.com"]                        | 1

        ["recipient@test.com", "recipient@test.com"] | []                                | []                                      | 1
        ["recipient@test.com"]                       | ["ccr@test.com", "ccr@test.com"]  | []                                      | 1

        ["recipient@test.com"]                       | ["recipient@test.com"]            | ["bccr@test.com", "bccr@test.com"]      | 2
        ["recipient@test.com"]                       | ["ccr@test.com", "bccr@test.com"] | ["ccr@test.com", "bccr@test.com"]       | 2

        ["recipient@test.com", "recipient@test.com"] | ["ccr@test.com", "ccr@test.com"]  | ["bccr@test.com", "bccr@test.com"]      | 3
        ["recipient@test.com", "ccr@test.com"]       | ["ccr@test.com", "bccr@test.com"] | ["recipient@test.com", "bccr@test.com"] | 3
    }
}
