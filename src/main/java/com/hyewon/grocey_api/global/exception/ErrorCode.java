package com.hyewon.grocey_api.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "Cart not found."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Cart item not found."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access is denied."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    FRIDGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Fridge not found"),
    FRIDGE_INGREDIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Fridge ingredient not found");



    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
