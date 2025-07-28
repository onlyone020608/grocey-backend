package com.hyewon.grocey_api.domain.recipe.repository;

import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
