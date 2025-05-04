package com.hyewon.grocey_api.domain.recipe;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String recipeName;

    private String description;

    private Integer cookingTime;

    private Integer servings;

    private LocalDateTime savedAt;



}
