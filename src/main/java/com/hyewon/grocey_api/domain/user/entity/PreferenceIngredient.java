package com.hyewon.grocey_api.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class PreferenceIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private PreferenceIngredient(String name) {
        this.name = name;
    }

    public static PreferenceIngredient of(String name) {
        return new PreferenceIngredient(name);
    }
}
