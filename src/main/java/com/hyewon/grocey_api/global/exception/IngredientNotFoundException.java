package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class IngredientNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.INGREDIENT_NOT_FOUND;

    public IngredientNotFoundException(String message) {
        super(message);
    }
    public IngredientNotFoundException(Long id) {
        super("Ingredient not found with id: " + id);
    }
}
