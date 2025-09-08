package com.hyewon.grocey_api.unit.domain.recipe;

import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeRepository;
import com.hyewon.grocey_api.domain.recipe.service.RecipeQueryService;
import com.hyewon.grocey_api.fixture.RecipeFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RecipeQueryServiceTest {
    @Mock RecipeRepository recipeRepository;
    @InjectMocks RecipeQueryService recipeQueryService;

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe =  RecipeFixture.aRecipe();
    }

    @Test
    @DisplayName("getRecipes - should return recipes")
    void getRecipes_shouldSucceed() {
        // given
        Long recipeId = 1L;
        given(recipeRepository.findAllById(List.of(recipeId))).willReturn(
               List.of(recipe));

        // when
        List<Recipe> resultRecipes = recipeQueryService.getRecipes(List.of(recipeId));

        // then
        assertThat(resultRecipes).isEqualTo(List.of(recipe));
    }
}
