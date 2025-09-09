package com.hyewon.grocey_api.unit.domain.recipe;

import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.domain.recipe.dto.RecipeDetailResponse;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.entity.RecipeIngredient;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeIngredientRepository;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeRepository;
import com.hyewon.grocey_api.domain.recipe.repository.SavedRecipeRepository;
import com.hyewon.grocey_api.domain.recipe.service.RecipeService;
import com.hyewon.grocey_api.fixture.RecipeFixture;
import com.hyewon.grocey_api.global.exception.RecipeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @Mock private RecipeRepository recipeRepository;
    @Mock private RecipeIngredientRepository recipeIngredientRepository;
    @Mock private SavedRecipeRepository savedRecipeRepository;
    @InjectMocks private RecipeService recipeService;

    private Recipe recipe;
    private RecipeIngredient recipeIngredient;

    @BeforeEach
    void setUp() {
        recipe = RecipeFixture.aRecipe();
        Ingredient ingredient = Ingredient.builder()
                .name("Kimchi")
                .imageUrl("url.com/kimchi")
                .build();
        recipeIngredient = RecipeIngredient.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .quantity("1 cup")
                .build();
    }

    @Test
    @DisplayName("returns detailed recipe with ingredients when recipe exists")
    void shouldReturnDetailedRecipeWithIngredients_whenRecipeExists() {
        // given
        Long userId = 1L;
        given(recipeRepository.findById(1L)).willReturn(Optional.of(recipe));
        given(recipeIngredientRepository.findAllByIdWithIngredient(1L)).willReturn(List.of(recipeIngredient));
        given(savedRecipeRepository.existsByUserIdAndRecipeId(userId, 1L)).willReturn(true);

        // when
        RecipeDetailResponse result = recipeService.getRecipeDetail(1L, userId);

        // then
        assertThat(result.getRecipeName()).isEqualTo("Kimchi Fried Rice");
        assertThat(result.getCookingTime()).isEqualTo(15);
        assertThat(result.getServings()).isEqualTo(2);
        assertThat(result.getDescriptionSteps()).containsExactly(
                "step1", "step2"
        );
        assertThat(result.getIngredients()).hasSize(1);
        assertThat(result.getIngredients().get(0).getName()).isEqualTo("Kimchi");
        assertThat(result.getIngredients().get(0).getQuantity()).isEqualTo("1 cup");
        assertThat(result.isSaved()).isTrue();
    }

    @Test
    @DisplayName("throws RecipeNotFoundException when recipe does not exist")
    void shouldThrowException_whenRecipeNotFound() {
        // given
        Long userId = 1L;
        given(recipeRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThrows(RecipeNotFoundException.class,
                () ->  recipeService.getRecipeDetail(999L, userId));
    }
}