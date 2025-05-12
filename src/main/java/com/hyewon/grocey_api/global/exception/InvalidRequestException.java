package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

    public InvalidRequestException(String message) {
        super(message);
    }
}
