package com.hyewon.grocey_api.domain.recipe.dto;

import lombok.Getter;

@Getter
public class RecipeIngredientDto {
    private String name;
    private String quantity;

    public RecipeIngredientDto(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

}
