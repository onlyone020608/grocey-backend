package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeRecommendation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id" )
    private Fridge fridge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private RecommendationType type;

    public RecipeRecommendation(User user, Recipe recipe, RecommendationType type) {
        this.recipe = recipe;
        this.user = user;
        this.type = type;
    }

    public RecipeRecommendation(Fridge fridge, Recipe recipe, RecommendationType type) {
        this.fridge = fridge;
        this.recipe = recipe;
        this.type = type;
    }
}
