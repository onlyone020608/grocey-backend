package com.hyewon.grocey_api.integration.recommendation;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import com.hyewon.grocey_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("RecipeRecommendationController Integration Test")
@Sql(scripts = "/sql/recipe-data.sql")
public class RecipeRecommendationControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/recipes/recommendations/personal - should return personal recipe recommendations")
    void getPersonalRecommendations_shouldReturnRecipes() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);

        Recipe recipe = recipeRepository.findById(1L).orElseThrow();
        setupRecipeRecommendationByUser(user, recipe);

        // when & then
        mockMvc.perform(get("/api/recipes/recommendations/personal")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeName").value(recipe.getRecipeName()))
                .andExpect(jsonPath("$[0].recipeImageUrl").value(recipe.getImageUrl()));
    }

    @Test
    @DisplayName("GET /api/recipes/recommendations/fridge - should return fridge-based recipe recommendations")
    void getFridgeRecommendations_shouldReturnRecipes() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);

        Recipe recipe = recipeRepository.findById(2L).orElseThrow();
        setupRecipeRecommendationByFridge(user, recipe);

        // when & then
        mockMvc.perform(get("/api/recipes/recommendations/fridge")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeName").value(recipe.getRecipeName()))
                .andExpect(jsonPath("$[0].recipeImageUrl").value(recipe.getImageUrl()));
    }
}
