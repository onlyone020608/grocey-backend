package com.hyewon.grocey_api.domain.recipe;

import com.hyewon.grocey_api.domain.recipe.dto.RecipeDetailResponseDto;
import com.hyewon.grocey_api.domain.recipe.dto.RecipeIngredientDto;
import com.hyewon.grocey_api.global.exception.RecipeNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final SavedRecipeRepository savedRecipeRepository;

    public RecipeDetailResponseDto getRecipeDetail(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        List<RecipeIngredientDto> ingredients = recipeIngredientRepository.findByRecipeId(recipeId).stream()
                .map(ri -> new RecipeIngredientDto(
                        ri.getIngredient().getIngredientName(),
                        ri.getQuantity()
                ))
                .collect(Collectors.toList());

        boolean isSaved = savedRecipeRepository.existsByUserIdAndRecipeId(userId, recipeId);

        return new RecipeDetailResponseDto(
                recipe.getRecipeName(),
                recipe.getDescription(),
                recipe.getCookingTime(),
                recipe.getServings(),
                recipe.getImageUrl(),
                ingredients,
                isSaved
        );
    }

}
