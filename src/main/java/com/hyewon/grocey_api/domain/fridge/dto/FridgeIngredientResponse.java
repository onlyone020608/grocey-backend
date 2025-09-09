package com.hyewon.grocey_api.domain.fridge.dto;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
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

    public static FridgeIngredientResponse from(FridgeIngredient fridgeIngredient) {
        return new FridgeIngredientResponse(
                fridgeIngredient.getIngredient().getId(),
                fridgeIngredient.getIngredient().getName(),
                fridgeIngredient.getIngredient().getImageUrl()
        );
    }
}

