package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class FridgeIngredientNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.FRIDGE_INGREDIENT_NOT_FOUND;

    public FridgeIngredientNotFoundException(Long id) {
        super("Fridge ingredient not found with id: " + id);
    }
}
