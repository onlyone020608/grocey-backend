package com.hyewon.grocey_api.domain.recipe.dto;

import com.hyewon.grocey_api.domain.recipe.SavedRecipe;
import lombok.Getter;

@Getter
public class SavedRecipeDto {

    private final Long recipeId;
    private final String recipeName;
    private final String imageUrl;

    public SavedRecipeDto(SavedRecipe savedRecipe) {
        this.recipeId = savedRecipe.getRecipe().getId();
        this.recipeName = savedRecipe.getRecipe().getRecipeName();
        this.imageUrl = savedRecipe.getRecipe().getImageUrl();
    }
}
