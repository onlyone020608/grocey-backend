package com.hyewon.grocey_api.domain.recipe.dto;

import com.hyewon.grocey_api.domain.recipe.entity.SavedRecipe;
import lombok.Getter;

@Getter
public class SavedRecipeResponse {

    private final Long recipeId;
    private final String recipeName;
    private final String imageUrl;

    public SavedRecipeResponse(SavedRecipe savedRecipe) {
        this.recipeId = savedRecipe.getRecipe().getId();
        this.recipeName = savedRecipe.getRecipe().getName();
        this.imageUrl = savedRecipe.getRecipe().getImageUrl();
    }
}
