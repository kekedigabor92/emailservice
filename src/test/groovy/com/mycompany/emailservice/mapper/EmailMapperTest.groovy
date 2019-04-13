package com.mycompany.emailservice.mapper

import com.mycompany.emailservice.domain.model.EmailDto
import spock.lang.Shared
import spock.lang.Specification

class EmailMapperTest extends Specification {

    @Shared
    EmailMapper emailMapper

    def setup() {
        emailMapper = new EmailMapper()
    }

    def "toEmail"() {
        given:
        def emailDto = new EmailDto(
                sender: "sender@test.com",
                recipients: recipients,
                carbonCopyRecipients: ccr,
                blindCarbonCopyRecipients: bccr
        )

        when:
        def mail = emailMapper.toEmail(emailDto)

        then:
        noExceptionThrown()

        and:
        mail
        !mail.personalization.empty
        mail.content

        where:
        recipients             | ccr              | bccr
        ["recipient@test.com"] | ["ccr@test.com"] | ["bccr@test.com"]
        ["recipient@test.com"] | null             | null
        ["recipient@test.com"] | null             | []
        ["recipient@test.com"] | []               | null
        ["recipient@test.com"] | []               | []
    }
}
