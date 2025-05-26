package com.hyewon.grocey_api.domain.fridge.dto;

import com.hyewon.grocey_api.domain.fridge.FridgeIngredient;
import lombok.Getter;

import java.time.LocalDate;
@Getter
public class FridgeIngredientDetailResponseDto {
        private String ingredientName;
        private String imageUrl;
        private int quantity;
        private Boolean isFreezer;
        private LocalDate expirationDate;

        public FridgeIngredientDetailResponseDto(FridgeIngredient fi) {
            this.ingredientName = fi.getIngredient().getIngredientName();
            this.imageUrl = fi.getIngredient().getImageUrl();
            this.quantity = fi.getQuantity();
            this.isFreezer = fi.getIsFreezer();
            this.expirationDate = fi.getExpirationDate();
        }

}
