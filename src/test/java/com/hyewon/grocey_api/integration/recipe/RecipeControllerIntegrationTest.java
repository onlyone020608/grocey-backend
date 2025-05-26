package com.hyewon.grocey_api.integration.recipe;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import com.hyewon.grocey_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("RecipeController Integration Test")
@Sql(scripts = "/sql/recipe-data.sql")
public class RecipeControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/recipes/{recipeId} - should return recipe detail")
    void getRecipeDetail_shouldReturnRecipeDetail() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "securepw");
        String token = generateTokenFor(user);
        Recipe recipe = recipeRepository.findById(1L).orElseThrow();


        // when & then
        mockMvc.perform(get("/api/recipes/" + recipe.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName").value(recipe.getRecipeName()))
                .andExpect(jsonPath("$.descriptionSteps[0]").exists())
                .andExpect(jsonPath("$.cookingTime").value(recipe.getCookingTime()))
                .andExpect(jsonPath("$.servings").value(recipe.getServings()))
                .andExpect(jsonPath("$.saved").value(false));
    }
}
