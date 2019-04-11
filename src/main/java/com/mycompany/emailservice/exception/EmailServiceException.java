package com.mycompany.emailservice.exception;

import com.mycompany.emailservice.model.ErrorDetailsDto;

public class EmailServiceException extends RuntimeException {

    private ErrorDetailsDto errorDetailsDto;

    public EmailServiceException(ErrorDetailsDto errorDetailsDto) {
        this.errorDetailsDto = errorDetailsDto;
    }

    public ErrorDetailsDto getErrorDetailsDto() {
        return errorDetailsDto;
    }

}
