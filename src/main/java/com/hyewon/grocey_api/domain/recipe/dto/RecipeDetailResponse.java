package com.hyewon.grocey_api.domain.recipe.dto;

import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.entity.RecipeIngredient;

import java.util.Arrays;
import java.util.List;

public record RecipeDetailResponse(
        String recipeName,
        List<String> descriptionSteps,
        Integer cookingTime,
        Integer servings,
        String recipeImageUrl,
        List<RecipeIngredientResponse> ingredients,
        boolean saved
) {
    public RecipeDetailResponse {
        if (descriptionSteps == null) {
            descriptionSteps = List.of();
        }
    }

    private static List<String> parseDescription(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split("\\n"))
                .map(String::trim)
                .toList();
    }

    public static RecipeDetailResponse from(Recipe recipe, List<RecipeIngredient> recipeIngredients, boolean saved) {
        return new RecipeDetailResponse(
                recipe.getName(),
                parseDescription(recipe.getDescription()),
                recipe.getCookingTimeInMinutes(),
                recipe.getServings(),
                recipe.getImageUrl(),
                recipeIngredients.stream()
                        .map(RecipeIngredientResponse::from)
                        .toList(),
                saved
        );
    }
}
