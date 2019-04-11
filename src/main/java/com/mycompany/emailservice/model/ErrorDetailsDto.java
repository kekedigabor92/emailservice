package com.mycompany.emailservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetailsDto {

    private LocalDateTime dateTime;
    private String message;
    private Object rejectedValue;

    public ErrorDetailsDto(LocalDateTime dateTime, String message, Object rejectedValue) {
        this.dateTime = dateTime;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }
}
