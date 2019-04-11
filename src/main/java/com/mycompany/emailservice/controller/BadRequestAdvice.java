package com.mycompany.emailservice.controller;

import com.mycompany.emailservice.model.ErrorDetailsDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RestController
public class BadRequestAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        Object rejectedValue = fieldError.getRejectedValue();
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(LocalDateTime.now(), defaultMessage, rejectedValue);
        return new ResponseEntity(errorDetailsDto, HttpStatus.BAD_REQUEST);
    }
}
