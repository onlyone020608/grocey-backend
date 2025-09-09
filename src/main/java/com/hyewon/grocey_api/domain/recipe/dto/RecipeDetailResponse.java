package com.hyewon.grocey_api.domain.recipe.dto;

import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.entity.RecipeIngredient;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class RecipeDetailResponse {
    private String recipeName;
    private List<String> descriptionSteps;
    private Integer cookingTime;
    private Integer servings;
    private List<RecipeIngredientResponse> ingredients;
    private String recipeImageUrl;
    private boolean saved;

    public RecipeDetailResponse(String recipeName, String description, Integer cookingTime,
                                Integer servings, String recipeImageUrl, List<RecipeIngredientResponse> ingredients, boolean saved) {
        this.recipeName = recipeName;
        this.descriptionSteps = parseDescription(description);
        this.cookingTime = cookingTime;
        this.servings = servings;
        this.recipeImageUrl = recipeImageUrl;
        this.ingredients = ingredients;
        this.saved = saved;
    }

    private List<String> parseDescription(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split("\\n"))
                .map(String::trim)
                .toList();
    }

    public static RecipeDetailResponse from (Recipe recipe, List<RecipeIngredient> recipeIngredients, boolean saved) {
        return new RecipeDetailResponse(
                recipe.getName(),
                recipe.getDescription(),
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
