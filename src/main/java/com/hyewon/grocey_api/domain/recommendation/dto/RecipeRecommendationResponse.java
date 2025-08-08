package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.entity.RecipeRecommendation;
import lombok.Getter;

@Getter
public class RecipeRecommendationResponse {
    private String recipeName;
    private String recipeImageUrl;
    private Long recipeId;

    public RecipeRecommendationResponse(RecipeRecommendation recommendation) {
        this.recipeId = recommendation.getRecipe().getId();
        this.recipeName = recommendation.getRecipe().getName();
        this.recipeImageUrl = recommendation.getRecipe().getImageUrl();
    }


}
