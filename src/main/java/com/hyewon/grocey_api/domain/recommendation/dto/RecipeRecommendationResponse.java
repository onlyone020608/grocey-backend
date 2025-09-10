package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.entity.RecipeRecommendation;
import lombok.Getter;

@Getter
public class RecipeRecommendationResponse {
    private String recipeName;
    private String recipeImageUrl;
    private Long recipeId;

    private RecipeRecommendationResponse(Long recipeId, String recipeName, String recipeImageUrl) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImageUrl = recipeImageUrl;
    }

    public static RecipeRecommendationResponse from(RecipeRecommendation recommendation) {
        return new RecipeRecommendationResponse(
                recommendation.getRecipe().getId(),
                recommendation.getRecipe().getName(),
                recommendation.getRecipe().getImageUrl()
        );
    }
}
