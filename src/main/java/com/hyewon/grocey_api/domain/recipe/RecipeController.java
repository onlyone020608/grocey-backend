package com.hyewon.grocey_api.domain.recipe;


import com.hyewon.grocey_api.domain.recipe.dto.RecipeDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping("/{recipeId}")
    public RecipeDetailResponseDto getRecipeDetail(@PathVariable Long recipeId) {
        return recipeService.getRecipeDetail(recipeId);
    }

}
