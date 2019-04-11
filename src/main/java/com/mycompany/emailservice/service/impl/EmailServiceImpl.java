package com.mycompany.emailservice.service.impl;

import com.mycompany.emailservice.model.EmailDto;
import com.mycompany.emailservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public EmailDto sendEmail(EmailDto email) {
        LOGGER.info("Sending email: {}", email);
        return email;
    }
}
