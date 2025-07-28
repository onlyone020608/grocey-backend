package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.entity.RecipeRecommendation;
import lombok.Getter;

@Getter
public class RecipeRecommendationDto {
    private String recipeName;
    private String recipeImageUrl;
    private Long recipeId;

    public RecipeRecommendationDto(RecipeRecommendation recommendation) {
        this.recipeId = recommendation.getRecipe().getId();
        this.recipeName = recommendation.getRecipe().getRecipeName();
        this.recipeImageUrl = recommendation.getRecipe().getImageUrl();
    }


}
