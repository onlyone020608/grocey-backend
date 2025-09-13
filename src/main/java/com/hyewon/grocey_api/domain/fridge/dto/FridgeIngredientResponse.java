package com.hyewon.grocey_api.domain.fridge.dto;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;

public record FridgeIngredientResponse(
        Long ingredientId,
        String ingredientName,
        String imageUrl
) {
    public static FridgeIngredientResponse from(FridgeIngredient fridgeIngredient) {
        return new FridgeIngredientResponse(
                fridgeIngredient.getId(),
                fridgeIngredient.getIngredient().getName(),
                fridgeIngredient.getIngredient().getImageUrl()
        );
    }
}

