package com.hyewon.grocey_api.domain.ingredient.repository;

import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    boolean existsByName(String ingredientName);
}
