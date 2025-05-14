package com.hyewon.grocey_api.integration.fridge;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("FridgeIngredientController Integration Test")
@Sql(scripts = {
        "/sql/ingredient-data.sql"
})
public class FridgeIngredientControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/fridge/ingredients - should return fridge ingredient list")
    void getFridgeIngredients_shouldReturnList() throws Exception {
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);

        Ingredient ingredient = ingredientRepository.findById(1L).orElseThrow();
        setupFridgeIngredient(user, ingredient, false, 2 );

        Long fridgeId = user.getFridge().getId();

        mockMvc.perform(get("/api/fridge/ingredients")
                        .header("Authorization", "Bearer " + token)
                        .param("isFreezer", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ingredientName").value(ingredient.getIngredientName()))
                .andExpect(jsonPath("$[0].imageUrl").value(ingredient.getImageUrl()));
    }

}
