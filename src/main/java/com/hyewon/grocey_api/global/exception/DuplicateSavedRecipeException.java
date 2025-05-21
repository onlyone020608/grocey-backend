package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class DuplicateSavedRecipeException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.DUPLICATE_SAVED_RECIPE;

    public DuplicateSavedRecipeException(Long recipeId) {
        super("Recipe already saved with id: " + recipeId);
    }
}
