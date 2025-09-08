package com.hyewon.grocey_api.fixture;

import com.hyewon.grocey_api.domain.recipe.entity.Recipe;

public class RecipeFixture {

    public static Recipe aRecipe() {
        return Recipe.builder()
                .id(10L)
                .name("Kimchi Fried Rice")
                .description("step1\nstep2")
                .cookingTimeInMinutes(15)
                .servings(2)
                .imageUrl("img.jpg")
                .build();
    }
}
