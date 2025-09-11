package com.hyewon.grocey_api.domain.recipe.dto;

import com.hyewon.grocey_api.domain.recipe.entity.RecipeIngredient;

public record RecipeIngredientResponse(
        String name,
        String quantity
) {
    public static RecipeIngredientResponse from(RecipeIngredient ri) {
        return new RecipeIngredientResponse(
                ri.getIngredient().getName(),
                ri.getQuantity()
        );
    }
}