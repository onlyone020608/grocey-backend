package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.ingredients.Ingredient;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class FridgeRecommendedIngredient extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_recommendation_id")
    private FridgeRecommendation fridgeRecommendation;

}
