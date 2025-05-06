package com.hyewon.grocey_api.domain.fridge.dto;

import lombok.Getter;

@Getter
public class FridgeIngredientResponseDto {
    String ingredientName;
    String imageUrl;

    public FridgeIngredientResponseDto(String ingredientName, String imageUrl) {
        this.ingredientName = ingredientName;
        this.imageUrl = imageUrl;
    }
}

