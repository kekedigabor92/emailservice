package com.mycompany.emailservice.exception.advice;

import com.mycompany.emailservice.domain.model.ErrorDetailsDto;
import com.mycompany.emailservice.exception.EmailServiceValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        Map<String, Object> errorVariables = new HashMap<>();
        errorVariables.put("Rejected value: ", fieldError.getRejectedValue());
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(HttpStatus.BAD_REQUEST, defaultMessage, errorVariables);
        return new ResponseEntity(errorDetailsDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EmailServiceValidationException.class})
    protected ResponseEntity<Object> handleEmailServiceException(RuntimeException ex, WebRequest request) {
        EmailServiceValidationException emailServiceValidationException = (EmailServiceValidationException) ex;
        ErrorDetailsDto errorDetailsDto = emailServiceValidationException.getErrorDetailsDto();
        return handleExceptionInternal(ex, errorDetailsDto, new HttpHeaders(), errorDetailsDto.getHttpStatus(), request);
    }

}
