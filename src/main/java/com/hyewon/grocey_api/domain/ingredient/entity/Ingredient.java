package com.hyewon.grocey_api.domain.ingredient.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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
