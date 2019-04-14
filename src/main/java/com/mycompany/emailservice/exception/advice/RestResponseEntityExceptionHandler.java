package com.mycompany.emailservice.exception.advice;

import com.mycompany.emailservice.domain.model.ErrorDetailsDto;
import com.mycompany.emailservice.exception.EmailServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    private static final String EXCEPTION_LOG_MESSAGE = "Exception occurred during processing request. ErrorDetails: {}";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        Map<String, Object> errorVariables = new HashMap<>();
        errorVariables.put("Rejected value", fieldError.getRejectedValue());
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(HttpStatus.BAD_REQUEST, defaultMessage, errorVariables);
        LOGGER.warn(EXCEPTION_LOG_MESSAGE, errorDetailsDto);
        return new ResponseEntity(errorDetailsDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> errorVariables = new HashMap<>();
        errorVariables.put("exceptionMessage", ex.getMessage());
        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(status, "JSON parse error. Cannot deserialize body.", errorVariables);
        LOGGER.warn(EXCEPTION_LOG_MESSAGE, errorDetailsDto);
        return handleExceptionInternal(ex, errorDetailsDto, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(value = {EmailServiceException.class})
    protected ResponseEntity<Object> handleEmailServiceException(RuntimeException ex, WebRequest request) {
        EmailServiceException emailServiceException = (EmailServiceException) ex;
        ErrorDetailsDto errorDetailsDto = emailServiceException.getErrorDetailsDto();
        LOGGER.warn(EXCEPTION_LOG_MESSAGE, errorDetailsDto);
        return handleExceptionInternal(ex, errorDetailsDto, new HttpHeaders(), errorDetailsDto.getHttpStatus(), request);
    }

}
