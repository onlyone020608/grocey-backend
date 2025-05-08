package com.hyewon.grocey_api.domain.recipe;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
