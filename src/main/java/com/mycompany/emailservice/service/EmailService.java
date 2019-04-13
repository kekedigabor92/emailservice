package com.mycompany.emailservice.service;

import com.mycompany.emailservice.domain.model.EmailDto;

public interface EmailService {

    EmailDto sendEmail(EmailDto email);
}
