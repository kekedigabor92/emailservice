package com.mycompany.emailservice.mapper

import spock.lang.Shared
import spock.lang.Specification

class EmailMapperTest extends Specification {

    @Shared
    EmailMapper emailMapper

    def setup() {
        emailMapper = new EmailMapper()
    }

}
