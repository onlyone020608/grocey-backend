package com.hyewon.grocey_api.domain.recipe;

import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.domain.recipe.dto.RecipeDetailResponseDto;
import com.hyewon.grocey_api.global.exception.RecipeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock private RecipeIngredientRepository recipeIngredientRepository;
    @InjectMocks
    private RecipeService recipeService;

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe(
                "Kimchi Fried Rice",
                "1. Put oil\n2. Add kimchi\n3. Stir-fry with rice",
                15,
                2
        );
        ReflectionTestUtils.setField(recipe, "id", 1L);
    }

    @Test
    @DisplayName("getRecipeDetail - returns detailed recipe with ingredients")
    void getRecipeDetail_shouldReturnDetail() {
        // given
        Ingredient ingredient = new Ingredient("Kimchi", "url.com/kimchi");
        RecipeIngredient ri = new RecipeIngredient(recipe, ingredient, "1 cup");

        given(recipeRepository.findById(1L)).willReturn(Optional.of(recipe));
        given(recipeIngredientRepository.findByRecipeId(1L)).willReturn(List.of(ri));

        // when
        RecipeDetailResponseDto result = recipeService.getRecipeDetail(1L);

        // then
        assertThat(result.getRecipeName()).isEqualTo("Kimchi Fried Rice");
        assertThat(result.getCookingTime()).isEqualTo(15);
        assertThat(result.getServings()).isEqualTo(2);
        assertThat(result.getDescriptionSteps()).containsExactly(
                "1. Put oil",
                "2. Add kimchi",
                "3. Stir-fry with rice"
        );
        assertThat(result.getIngredients()).hasSize(1);
        assertThat(result.getIngredients().get(0).getName()).isEqualTo("Kimchi");
        assertThat(result.getIngredients().get(0).getQuantity()).isEqualTo("1 cup");
    }

    @Test
    @DisplayName("getRecipeDetail - throws RecipeNotFoundException when recipe not found")
    void getRecipeDetail_shouldThrowIfRecipeNotFound() {
        // given
        given(recipeRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> recipeService.getRecipeDetail(999L))
                .isInstanceOf(RecipeNotFoundException.class);
    }

}