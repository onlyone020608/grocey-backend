package com.hyewon.grocey_api.domain.fridge.dto;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;

import java.time.LocalDate;

public record FridgeIngredientDetailResponse(
        String ingredientName,
        String imageUrl,
        int quantity,
        Boolean isFreezer,
        LocalDate expirationDate
) {
    public static FridgeIngredientDetailResponse from(FridgeIngredient fridgeIngredient) {
        return new FridgeIngredientDetailResponse(
                fridgeIngredient.getIngredient().getName(),
                fridgeIngredient.getIngredient().getImageUrl(),
                fridgeIngredient.getQuantity(),
                fridgeIngredient.getFreezer(),
                fridgeIngredient.getExpirationDate()
        );
    }
}