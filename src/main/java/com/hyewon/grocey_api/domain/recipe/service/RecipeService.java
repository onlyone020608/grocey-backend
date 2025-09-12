package com.hyewon.grocey_api.domain.recipe.service;

import com.hyewon.grocey_api.domain.recipe.dto.RecipeDetailResponse;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.entity.RecipeIngredient;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeIngredientRepository;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeRepository;
import com.hyewon.grocey_api.domain.recipe.repository.SavedRecipeRepository;
import com.hyewon.grocey_api.global.exception.RecipeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final SavedRecipeRepository savedRecipeRepository;

    @Cacheable(value = "recipes", key = "#recipeId")
    @Transactional(readOnly = true)
    public RecipeDetailResponse getRecipeDetail(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllByIdWithIngredient(recipeId);

        boolean isSaved = savedRecipeRepository.existsByUserIdAndRecipeId(userId, recipeId);

        return RecipeDetailResponse.from(recipe, recipeIngredients, isSaved);
    }
}
