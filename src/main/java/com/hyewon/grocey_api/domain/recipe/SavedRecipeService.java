package com.hyewon.grocey_api.domain.recipe;

import com.hyewon.grocey_api.domain.recipe.dto.SavedRecipeDto;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.DuplicateSavedRecipeException;
import com.hyewon.grocey_api.global.exception.RecipeNotFoundException;
import com.hyewon.grocey_api.global.exception.SavedRecipeNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedRecipeService {
    private final SavedRecipeRepository savedRecipeRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public List<SavedRecipeDto> getSavedRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<SavedRecipe> savedRecipes = savedRecipeRepository.findByUser(user);

        return savedRecipes.stream()
                .map(SavedRecipeDto::new)
                .toList();

    }

    @Transactional
    public void saveRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        boolean exists = savedRecipeRepository.existsByUserAndRecipe(user, recipe);
        if (exists) {
            throw new DuplicateSavedRecipeException(recipeId);
        }

        SavedRecipe savedRecipe = new SavedRecipe(user, recipe);
        savedRecipeRepository.save(savedRecipe);
    }

    @Transactional
    public void deleteRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        SavedRecipe savedRecipe = savedRecipeRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new SavedRecipeNotFoundException(userId, recipeId));

        savedRecipeRepository.delete(savedRecipe);
    }
}
