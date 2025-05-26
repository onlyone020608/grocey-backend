package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class SavedRecipeNotFoundException extends RuntimeException{
    private final ErrorCode errorCode = ErrorCode.SAVED_RECIPE_NOT_FOUND;

    public SavedRecipeNotFoundException(Long userId, Long recipeId) {
        super("Saved recipe not found for userId: " + userId + ", recipeId: " + recipeId);
    }
}
