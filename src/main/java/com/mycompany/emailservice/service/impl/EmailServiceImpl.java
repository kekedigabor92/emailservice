package com.mycompany.emailservice.service.impl;

import com.mycompany.emailservice.client.EmailClient;
import com.mycompany.emailservice.domain.model.EmailDto;
import com.mycompany.emailservice.domain.model.ErrorDetailsDto;
import com.mycompany.emailservice.domain.validator.EmailValidator;
import com.mycompany.emailservice.exception.EmailServiceException;
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

    private final EmailMapper emailMapper;
    private final EmailValidator emailValidator;
    private final EmailClient emailClient;

    public EmailServiceImpl(EmailValidator emailValidator, EmailMapper emailMapper, EmailClient emailClient) {
        this.emailMapper = emailMapper;
        this.emailValidator = emailValidator;
        this.emailClient = emailClient;
    }

    @Override
    public EmailDto sendEmail(EmailDto email) {
        String sender = email.getSender();
        LOGGER.info("Validating email (sender: {})", sender);
        emailValidator.validateEmail(email);

        LOGGER.info("Sending email (sender: {})", sender);
        Mail mail = emailMapper.toEmail(email);

        try {
            Response response = emailClient.send(mail);
            checkResponse(response);
            LOGGER.info("Email was queued to be delivered (sender: {})", sender);
        } catch (IOException ex) {
            propagateException(ex);
        }
        return email;
    }

    private void checkResponse(Response response) {
        if (response == null || HttpStatus.ACCEPTED.value() != response.getStatusCode()) {
            Map<String, Object> errorVariables = new HashMap<>();
            errorVariables.put("Response from email service provider: ", response);
            ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(HttpStatus.INTERNAL_SERVER_ERROR, "Sending email was not successful. Please try again.", errorVariables);
            throw new EmailServiceException(errorDetailsDto);
        }
    }

    private void propagateException(IOException ex) {
        Map<String, Object> errorVariables = new HashMap<>();
        errorVariables.put("Message", ex.getMessage());
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(HttpStatus.BAD_REQUEST, "Unexpected error occurred during sending an e-mail.", errorVariables);
        throw new EmailServiceException(errorDetailsDto);
    }
}
