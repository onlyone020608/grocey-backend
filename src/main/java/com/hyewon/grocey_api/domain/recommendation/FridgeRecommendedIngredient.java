package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.ingredients.Ingredient;
import jakarta.persistence.*;

@Entity
public class FridgeRecommendedIngredient {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    private FridgeRecommendation fridgeRecommendation;

}
