package com.mycompany.emailservice.client

import com.sendgrid.Mail
import com.sendgrid.SendGrid
import spock.lang.Shared
import spock.lang.Specification

class EmailClientTest extends Specification {

    @Shared
    SendGrid sendGrid

    @Shared
    EmailClient emailClient

    def setup() {
        sendGrid = Mock(SendGrid)
        emailClient = new EmailClient(sendGrid)
    }

    def "send - positive"() {
        given:
        def mail = new Mail()

        when:
        emailClient.send(mail)

        then:
        noExceptionThrown()
        and:
        1 * sendGrid.api(_)
    }

    def "send - throws exception"() {
        given:
        def mail = new Mail()

        when:
        emailClient.send(mail)

        then:
        1 * sendGrid.api(_) >> { throw new IOException() }
        and:
        thrown(IOException)
    }
}
