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

    private String name;
    private String description;
    private Integer cookingTimeInMinutes;
    private Integer servings;
    private String imageUrl;
    private LocalDateTime savedAt;

    private Recipe(String name, String description, Integer cookingTimeInMinutes, Integer servings, String imageUrl) {
        this.name = name;
        this.description = description;
        this.cookingTimeInMinutes = cookingTimeInMinutes;
        this.servings = servings;
        this.imageUrl = imageUrl;
    }

    public static Recipe of(String name,  String description, Integer cookingTimeInMinutes, Integer servings, String imageUrl) {
        return new Recipe(name, description, cookingTimeInMinutes, servings, imageUrl);
    }
}
