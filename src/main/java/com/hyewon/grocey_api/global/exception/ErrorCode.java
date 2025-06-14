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
    FRIDGE_INGREDIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Fridge ingredient not found"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request."),
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "Invalid enum value provided."),
    RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Recipe not found."),
    DUPLICATE_SAVED_RECIPE(HttpStatus.CONFLICT, "Recipe already saved."),
    SAVED_RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Saved recipe not found.");




    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
