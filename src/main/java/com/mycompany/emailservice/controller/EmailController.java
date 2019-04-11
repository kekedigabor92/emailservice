package com.mycompany.emailservice.controller;

import com.mycompany.emailservice.model.EmailDto;
import com.mycompany.emailservice.service.EmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @ApiOperation(value = "Send an email")
    @PostMapping("/emails")
    public EmailDto sendEmail(@ApiParam(value = "Email object", required = true)
                              @Valid
                              @RequestBody
                                      EmailDto email) {
        return emailService.sendEmail(email);
    }
}
