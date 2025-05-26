package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class CartNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.CART_NOT_FOUND;

    public CartNotFoundException(Long userId) {
        super("Cart not found for userId: " + userId);
    }
}
