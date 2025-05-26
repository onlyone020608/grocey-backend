package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class RecipeNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.RECIPE_NOT_FOUND;

    public RecipeNotFoundException(Long id) {
        super("Recipe not found. (id=" + id + ")");
    }

}
