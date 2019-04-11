package com.mycompany.emailservice.service;

import com.mycompany.emailservice.model.EmailDto;

public interface EmailService {

    EmailDto sendEmail(EmailDto email);
}
