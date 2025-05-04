package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RecipeRecommendation {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id" )
    private Fridge fridge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private LocalDateTime recommendedAt;

    @Enumerated(EnumType.STRING)
    private RecommendationType type;


}
