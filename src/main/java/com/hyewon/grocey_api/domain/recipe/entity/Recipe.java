package com.hyewon.grocey_api.domain.recipe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String recipeName;

    private String description;

    private Integer cookingTime;

    private Integer servings;

    private String imageUrl;

    private LocalDateTime savedAt;

    public Recipe(String recipeName, String description, Integer cookingTime, Integer servings) {
        this.recipeName = recipeName;
        this.description = description;
        this.cookingTime = cookingTime;
        this.servings = servings;
    }

    public Recipe(String recipeName, String description, Integer cookingTime, Integer servings, String imageUrl) {
        this.recipeName = recipeName;
        this.description = description;
        this.cookingTime = cookingTime;
        this.servings = servings;
        this.imageUrl = imageUrl;
    }
}
