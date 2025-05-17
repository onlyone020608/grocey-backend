package com.hyewon.grocey_api.domain.fridge.dto;

import lombok.Getter;

@Getter
public class FridgeIngredientResponseDto {
    Long ingredientId;
    String ingredientName;
    String imageUrl;

    public FridgeIngredientResponseDto(Long id, String ingredientName, String imageUrl) {
        this.ingredientId = id;
        this.ingredientName = ingredientName;
        this.imageUrl = imageUrl;
    }
}

