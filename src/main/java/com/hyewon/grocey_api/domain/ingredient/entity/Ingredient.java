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

    private String name;

    private String imageUrl;

    private Ingredient(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static Ingredient of(String name, String imageUrl) {
        return new Ingredient(name, imageUrl);
    }
}
