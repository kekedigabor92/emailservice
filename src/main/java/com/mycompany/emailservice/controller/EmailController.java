package com.mycompany.emailservice.controller;

import com.mycompany.emailservice.domain.model.EmailDto;
import com.mycompany.emailservice.domain.model.ErrorDetailsDto;
import com.mycompany.emailservice.service.EmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
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
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted - The e-mail was successfully queued to be delivered."),
            @ApiResponse(code = 400, message = "Bad request - The request is invalid. ", response = ErrorDetailsDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error - Temporary error. Please try again.", response = ErrorDetailsDto.class)
    })
    @PostMapping("/emails")
    public ResponseEntity<EmailDto> sendEmail(@ApiParam(value = "Email object", required = true)
                                              @Valid
                                              @RequestBody EmailDto email) {
        EmailDto emailDto = emailService.sendEmail(email);
        return ResponseEntity.accepted().body(emailDto);
    }
}
