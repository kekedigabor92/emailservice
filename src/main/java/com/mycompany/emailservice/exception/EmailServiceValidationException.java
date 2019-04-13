package com.mycompany.emailservice.exception;

import com.mycompany.emailservice.domain.model.ErrorDetailsDto;

public class EmailServiceValidationException extends RuntimeException {

    private ErrorDetailsDto errorDetailsDto;

    public EmailServiceValidationException(ErrorDetailsDto errorDetailsDto) {
        this.errorDetailsDto = errorDetailsDto;
    }

    public ErrorDetailsDto getErrorDetailsDto() {
        return errorDetailsDto;
    }

}
