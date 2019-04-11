package com.mycompany.emailservice.service.impl;

import com.mycompany.emailservice.client.EmailClient;
import com.mycompany.emailservice.mapper.EmailMapper;
import com.mycompany.emailservice.model.EmailDto;
import com.mycompany.emailservice.service.EmailService;
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
        Mail mail = emailMapper.toEmail(email);
        LOGGER.info("Sending email: {}", email);
        try {
            Response send = emailClient.send(mail);
            System.out.println(send.getStatusCode());

        } catch (IOException ex) {
            System.out.println("error occcured");
        }
        return email;
    }
}
