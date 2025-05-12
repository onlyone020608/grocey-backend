package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class InvalidEnumValueException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.INVALID_ENUM_VALUE;

    public InvalidEnumValueException(String value, String enumType) {
        super(String.format("Invalid value '%s' for enum type %s.", value, enumType));
    }
}
