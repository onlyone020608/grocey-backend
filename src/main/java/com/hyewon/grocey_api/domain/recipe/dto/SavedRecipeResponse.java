package com.hyewon.grocey_api.domain.recipe.dto;

import com.hyewon.grocey_api.domain.recipe.entity.SavedRecipe;
import lombok.Getter;

@Getter
public class SavedRecipeResponse {

    private final Long recipeId;
    private final String recipeName;
    private final String imageUrl;

    private SavedRecipeResponse(Long recipeId, String recipeName, String imageUrl) {
        this.recipeId =  recipeId;
        this.recipeName = recipeName;
        this.imageUrl = imageUrl;
    }

    public static SavedRecipeResponse from(SavedRecipe savedRecipe) {
        return new SavedRecipeResponse(
                savedRecipe.getRecipe().getId(),
                savedRecipe.getRecipe().getName(),
                savedRecipe.getRecipe().getImageUrl());
    }
}
