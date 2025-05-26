package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class CartItemNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.CART_ITEM_NOT_FOUND;

    public CartItemNotFoundException(Long cartItemId) {
        super("Cart item not found with id: " + cartItemId);
    }

}
