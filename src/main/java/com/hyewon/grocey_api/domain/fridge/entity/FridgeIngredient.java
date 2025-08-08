package com.hyewon.grocey_api.domain.fridge.entity;

import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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

    private Boolean freezer;
    private int quantity;
    private LocalDate expirationDate;

    public FridgeIngredient(Fridge fridge, Ingredient ingredient, Boolean freezer, int quantity, LocalDate expirationDate) {
        this.fridge = fridge;
        this.ingredient = ingredient;
        this.freezer = freezer;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }
}
