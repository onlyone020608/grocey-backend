package com.hyewon.grocey_api.domain.recipe.dto;

import com.hyewon.grocey_api.domain.recipe.entity.SavedRecipe;

public record SavedRecipeResponse(
        Long recipeId,
        String recipeName,
        String imageUrl
) {
    public static SavedRecipeResponse from(SavedRecipe savedRecipe) {
        return new SavedRecipeResponse(
                savedRecipe.getRecipe().getId(),
                savedRecipe.getRecipe().getName(),
                savedRecipe.getRecipe().getImageUrl()
        );
    }
}
