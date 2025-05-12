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

    public RecipeDetailResponseDto getRecipeDetail(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));

        List<RecipeIngredientDto> ingredients = recipeIngredientRepository.findByRecipeId(id).stream()
                .map(ri -> new RecipeIngredientDto(
                        ri.getIngredient().getIngredientName(),
                        ri.getQuantity()
                ))
                .collect(Collectors.toList());

        return new RecipeDetailResponseDto(
                recipe.getRecipeName(),
                recipe.getDescription(),
                recipe.getCookingTime(),
                recipe.getServings(),
                ingredients
        );
    }

}
