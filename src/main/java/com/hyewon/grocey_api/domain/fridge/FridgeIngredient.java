package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.ingredients.Ingredient;
import com.hyewon.grocey_api.domain.user.FoodPreference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class FridgeIngredient {
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


}
