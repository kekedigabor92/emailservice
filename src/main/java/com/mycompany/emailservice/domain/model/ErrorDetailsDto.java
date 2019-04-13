package com.mycompany.emailservice.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class ErrorDetailsDto {

    private LocalDateTime dateTime;
    private HttpStatus httpStatus;
    private String message;
    private Map<String, Object> errorVariable;

    public ErrorDetailsDto(HttpStatus httpStatus, String message, Map<String, Object> errorVariable) {
        this.dateTime = LocalDateTime.now();
        this.httpStatus = httpStatus;
        this.message = message;
        this.errorVariable = errorVariable;
    }

    public ErrorDetailsDto(LocalDateTime dateTime, HttpStatus httpStatus, String message, Map<String, Object> errorVariable) {
        this.dateTime = dateTime;
        this.httpStatus = httpStatus;
        this.message = message;
        this.errorVariable = errorVariable;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getErrorVariable() {
        return errorVariable;
    }
}
