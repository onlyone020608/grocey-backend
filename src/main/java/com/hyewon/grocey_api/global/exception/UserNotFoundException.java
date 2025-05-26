package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;

    public UserNotFoundException(Long userId) {
        super("User not found with id: " + userId);
    }

}
