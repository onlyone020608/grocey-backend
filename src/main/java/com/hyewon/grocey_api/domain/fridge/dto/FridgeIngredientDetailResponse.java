package com.hyewon.grocey_api.domain.fridge.dto;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FridgeIngredientDetailResponse {
        private String ingredientName;
        private String imageUrl;
        private int quantity;
        private Boolean isFreezer;
        private LocalDate expirationDate;

        public FridgeIngredientDetailResponse(FridgeIngredient fridgeIngredient) {
            this.ingredientName = fridgeIngredient.getIngredient().getName();
            this.imageUrl = fridgeIngredient.getIngredient().getImageUrl();
            this.quantity = fridgeIngredient.getQuantity();
            this.isFreezer = fridgeIngredient.getFreezer();
            this.expirationDate = fridgeIngredient.getExpirationDate();
        }
}
