package com.mycompany.emailservice.service.impl;

import com.mycompany.emailservice.client.EmailClient;
import com.mycompany.emailservice.service.EmailService;
import com.mycompany.emailservice.mapper.EmailMapper;
import com.mycompany.emailservice.domain.model.EmailDto;
import com.sendgrid.Mail;
import com.sendgrid.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private EmailMapper emailMapper;
    private EmailClient emailClient;

    public EmailServiceImpl(EmailMapper emailMapper, EmailClient emailClient) {
        this.emailMapper = emailMapper;
        this.emailClient = emailClient;
    }

    @Override
    public EmailDto sendEmail(EmailDto email) {
        LOGGER.info("Mapping email: {}", email);
        Mail mail = emailMapper.toEmail(email);
        LOGGER.info("Sending mapped email: {}", mail);
        try {
            Response send = emailClient.send(mail);
            System.out.println(send.getStatusCode());

        } catch (IOException ex) {
           // TODO handle exception properly
        }
        return email;
    }
}
