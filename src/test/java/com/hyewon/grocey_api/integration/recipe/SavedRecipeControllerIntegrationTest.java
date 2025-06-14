package com.hyewon.grocey_api.integration.recipe;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import com.hyewon.grocey_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SavedRecipeController Integration Test")
@Sql(scripts = "/sql/recipe-data.sql")
public class SavedRecipeControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/users/me/recipes - should return saved recipes of user")
    void getSavedRecipes_shouldReturnRecipes() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@", "securepw");
        String token = generateTokenFor(user);

        Recipe recipe = recipeRepository.findById(1L).orElseThrow();
        setupSavedRecipe(user, recipe);

        // when & then
        mockMvc.perform(get("/api/users/me/recipes")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeId").value(recipe.getId()))
                .andExpect(jsonPath("$[0].recipeName").value(recipe.getRecipeName()))
                .andExpect(jsonPath("$[0].imageUrl").value(recipe.getImageUrl()));
    }

    @Test
    @DisplayName("POST /api/users/me/recipes - should save recipe successfully")
    void saveRecipe_shouldSucceed() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@", "securepw");
        String token = generateTokenFor(user);

        Recipe recipe = recipeRepository.findById(1L).orElseThrow();

        // when & then
        mockMvc.perform(post("/api/users/me/recipes/" + recipe.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }
    @Test
    @DisplayName("DELETE /api/users/me/recipes/{recipeId} - should delete saved recipe")
    void deleteSavedRecipe_shouldSucceed() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@", "securepw");
        String token = generateTokenFor(user);

        Recipe recipe = recipeRepository.findById(1L).orElseThrow();
        setupSavedRecipe(user, recipe);

        // when & then
        mockMvc.perform(delete("/api/users/me/recipes/" + recipe.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/me/recipes")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

}
