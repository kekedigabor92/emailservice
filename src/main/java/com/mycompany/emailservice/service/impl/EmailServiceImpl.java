package com.mycompany.emailservice.service.impl;

import com.mycompany.emailservice.client.EmailClient;
import com.mycompany.emailservice.domain.model.EmailDto;
import com.mycompany.emailservice.domain.model.ErrorDetailsDto;
import com.mycompany.emailservice.domain.validator.EmailValidator;
import com.mycompany.emailservice.exception.EmailServiceValidationException;
import com.mycompany.emailservice.mapper.EmailMapper;
import com.mycompany.emailservice.service.EmailService;
import com.sendgrid.Mail;
import com.sendgrid.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private EmailMapper emailMapper;
    private EmailValidator emailValidator;
    private EmailClient emailClient;

    public EmailServiceImpl(EmailValidator emailValidator, EmailMapper emailMapper, EmailClient emailClient) {
        this.emailMapper = emailMapper;
        this.emailValidator = emailValidator;
        this.emailClient = emailClient;
    }

    @Override
    public EmailDto sendEmail(EmailDto email) {
        LOGGER.info("Validating email: {}", email);
        emailValidator.validateEmail(email);

        LOGGER.info("Sending email: {}", email);
        Mail mail = emailMapper.toEmail(email);

        try {
            Response send = emailClient.send(mail);
        } catch (IOException ex) {
            Map<String, Object> errorVariables = new HashMap<>();
            errorVariables.put("Message", ex.getMessage());
            ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(HttpStatus.BAD_REQUEST, "Unexpected error occurred during sending an e-mail.", errorVariables);
            throw new EmailServiceValidationException(errorDetailsDto);
        }
        return email;
    }
}
