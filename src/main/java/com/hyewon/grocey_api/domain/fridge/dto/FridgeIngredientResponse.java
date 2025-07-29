package com.hyewon.grocey_api.domain.fridge.dto;

import lombok.Getter;

@Getter
public class FridgeIngredientResponse {
    Long ingredientId;
    String ingredientName;
    String imageUrl;

    public FridgeIngredientResponse(Long id, String ingredientName, String imageUrl) {
        this.ingredientId = id;
        this.ingredientName = ingredientName;
        this.imageUrl = imageUrl;
    }
}

