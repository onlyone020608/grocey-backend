package com.hyewon.grocey_api.global.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long userId) {
        super("Cart not found for userId: " + userId);
    }
}
