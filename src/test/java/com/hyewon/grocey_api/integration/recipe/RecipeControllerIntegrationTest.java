package com.hyewon.grocey_api.integration.recipe;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("RecipeController Integration Test")
public class RecipeControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/recipes/{recipeId} - returns recipe detail when id is valid")
    void getRecipeDetail_withValidId_returnsRecipeDetail() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);
        Recipe recipe = recipeRepository.findById(1L).orElseThrow();

        // when & then
        mockMvc.perform(get("/api/recipes/" + recipe.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName").value(recipe.getName()))
                .andExpect(jsonPath("$.descriptionSteps[0]").exists())
                .andExpect(jsonPath("$.cookingTime").value(recipe.getCookingTimeInMinutes()))
                .andExpect(jsonPath("$.servings").value(recipe.getServings()))
                .andExpect(jsonPath("$.saved").value(false));
    }
}
