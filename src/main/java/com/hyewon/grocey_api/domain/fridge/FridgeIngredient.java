package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FridgeIngredient extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private Boolean isFreezer;
    private int quantity;
    private LocalDate expirationDate;

    public FridgeIngredient(Fridge fridge, Ingredient ingredient, Boolean isFreezer, int quantity, LocalDate expirationDate) {
        this.fridge = fridge;
        this.ingredient = ingredient;
        this.isFreezer = isFreezer;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }
}
