package com.hyewon.grocey_api.domain.recipe.dto;

import com.hyewon.grocey_api.domain.recipe.Recipe;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class RecipeDetailResponseDto {

    private String recipeName;
    private List<String> descriptionSteps;
    private Integer cookingTime;
    private Integer servings;
    private List<RecipeIngredientDto> ingredients;
    private String recipeImageUrl;
    private boolean saved;

    public RecipeDetailResponseDto(String recipeName, String description, Integer cookingTime,
                                   Integer servings, String recipeImageUrl,  List<RecipeIngredientDto> ingredients,  boolean saved) {
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
}
