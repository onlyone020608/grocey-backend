package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(ErrorCode code) {
        this.status = code.getStatus().value();
        this.error = code.name();
        this.message = code.getMessage();
        this.timestamp = LocalDateTime.now();
    }


}
