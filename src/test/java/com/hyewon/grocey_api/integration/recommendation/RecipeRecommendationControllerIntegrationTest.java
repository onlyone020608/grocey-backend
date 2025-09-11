package com.hyewon.grocey_api.integration.recommendation;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("RecipeRecommendationController Integration Test")
public class RecipeRecommendationControllerIntegrationTest extends AbstractIntegrationTest {
    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    @DisplayName("GET /api/recipes/recommendations/personal - returns personal recipe recommendations when user has preferences")
    void getPersonalRecommendations_withUserPreferences_returnsRecipes() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);

        Recipe recipe = recipeRepository.findById(1L).orElseThrow();

        List<Long> aiReturnedIds = List.of(recipe.getId());
        String url = "http://grocey-ai:5001/api/recommend/recipes/preference/" + user.getId();
        ResponseEntity<List> mockResponse = new ResponseEntity<>(aiReturnedIds, HttpStatus.OK);
        given(restTemplate.getForEntity(url, List.class)).willReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/recipes/recommendations/personal")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeId").exists())
                .andExpect(jsonPath("$[0].recipeName").value(recipe.getName()))
                .andExpect(jsonPath("$[0].recipeImageUrl").value(recipe.getImageUrl()));
    }

    @Test
    @DisplayName("GET /api/recipes/recommendations/fridge - returns fridge-based recipe recommendations when user has ingredients")
    void getFridgeRecommendations_withFridgeIngredients_returnsRecipes() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);

        Recipe recipe = recipeRepository.findById(2L).orElseThrow();

        List<Long> aiReturnedIds = List.of(recipe.getId());
        String url = "http://grocey-ai:5001/api/recommend/recipes/fridge/" + user.getId();
        ResponseEntity<List> mockResponse = new ResponseEntity<>(aiReturnedIds, HttpStatus.OK);
        given(restTemplate.getForEntity(url, List.class)).willReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/recipes/recommendations/fridge")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeId").exists())
                .andExpect(jsonPath("$[0].recipeName").value(recipe.getName()))
                .andExpect(jsonPath("$[0].recipeImageUrl").value(recipe.getImageUrl()));
    }
}
