package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.entity.RecipeRecommendation;

public record RecipeRecommendationResponse(
        Long recipeId,
        String recipeName,
        String recipeImageUrl
) {
    public static RecipeRecommendationResponse from(RecipeRecommendation recommendation) {
        return new RecipeRecommendationResponse(
                recommendation.getRecipe().getId(),
                recommendation.getRecipe().getName(),
                recommendation.getRecipe().getImageUrl()
        );
    }
}
