package com.hyewon.grocey_api.domain.ingredient;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String ingredientName;

    private String imageUrl;

    public Ingredient(String ingredientName, String imageUrl) {
        this.ingredientName = ingredientName;
        this.imageUrl = imageUrl;
    }
}
