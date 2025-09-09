package com.hyewon.grocey_api.domain.recipe.service;

import com.hyewon.grocey_api.domain.recipe.dto.SavedRecipeResponse;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.entity.SavedRecipe;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeRepository;
import com.hyewon.grocey_api.domain.recipe.repository.SavedRecipeRepository;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.global.exception.DuplicateSavedRecipeException;
import com.hyewon.grocey_api.global.exception.RecipeNotFoundException;
import com.hyewon.grocey_api.global.exception.SavedRecipeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedRecipeService {
    private final SavedRecipeRepository savedRecipeRepository;
    private final UserQueryService userQueryService;
    private final RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public List<SavedRecipeResponse> getSavedRecipes(Long userId) {
        List<SavedRecipe> savedRecipes = savedRecipeRepository.findByUserIdWithRecipe(userId);

        return savedRecipes.stream()
                .map(SavedRecipeResponse::from)
                .toList();
    }

    @Transactional
    public void saveRecipe(Long userId, Long recipeId) {
        User user = userQueryService.getUserById(userId);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        boolean exists = savedRecipeRepository.existsByUserAndRecipe(user, recipe);
        if (exists) {
            throw new DuplicateSavedRecipeException(recipeId);
        }

        SavedRecipe savedRecipe = SavedRecipe.of(user, recipe);
        savedRecipeRepository.save(savedRecipe);
    }

    @Transactional
    public void deleteRecipe(Long userId, Long recipeId) {
        SavedRecipe savedRecipe = savedRecipeRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new SavedRecipeNotFoundException(userId, recipeId));

        savedRecipeRepository.delete(savedRecipe);
    }
}
