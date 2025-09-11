package com.hyewon.grocey_api.unit.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.service.RecipeQueryService;
import com.hyewon.grocey_api.domain.recommendation.dto.RecipeRecommendationResponse;
import com.hyewon.grocey_api.domain.recommendation.repository.RecipeRecommendationRepository;
import com.hyewon.grocey_api.domain.recommendation.service.RecipeRecommendationService;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.fixture.FridgeFixture;
import com.hyewon.grocey_api.fixture.RecipeFixture;
import com.hyewon.grocey_api.fixture.UserFixture;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecipeRecommendationServiceTest {
    @Mock private RecipeRecommendationRepository recipeRecommendationRepository;
    @Mock private UserQueryService userQueryService;
    @Mock private RestTemplate restTemplate;
    @Mock private FridgeQueryService fridgeQueryService;
    @Mock private RecipeQueryService recipeQueryService;
    @InjectMocks private RecipeRecommendationService recipeRecommendationService;

    private User user;
    private Fridge fridge;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        user = UserFixture.aDefaultUser();
        fridge = FridgeFixture.aFridge();
        user.assignFridge(fridge);
        recipe =  RecipeFixture.aRecipe();
    }

    @Test
    @DisplayName("returns recipe recommendations when AI returns recipe IDs for user")
    void shouldReturnRecommendations_whenAiReturnsRecipeIdsForUser() {
        // given
        Long userId = 1L;
        List<Long> aiReturnedIds = List.of(101L, 102L);

        String url = "http://grocey-ai:5001/api/recommend/recipes/preference/" + userId;
        ResponseEntity<List> mockResponse = new ResponseEntity<>(aiReturnedIds, HttpStatus.OK);
        given(restTemplate.getForEntity(url, List.class)).willReturn(mockResponse);
        given(userQueryService.getUserById(userId)).willReturn(user);
        given(recipeQueryService.getRecipes(aiReturnedIds)).willReturn(List.of(recipe));
        given(recipeRecommendationRepository.saveAll(anyList()))
                .willAnswer(invocation -> invocation.getArgument(0));

        List<RecipeRecommendationResponse> result = recipeRecommendationService.getRecommendationsByUser(userId);

        // then
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("throws RecommendationNotFoundException when no user-based recommendations are found")
    void shouldThrowException_whenUserRecommendationsNotFound() {
        // given
        Long userId = 1L;
        given(userQueryService.getUserById(userId)).willReturn(user);
        String url = "http://grocey-ai:5001/api/recommend/recipes/preference/" + userId;
        given(restTemplate.getForEntity(url, List.class))
                .willReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

        // when & then
        assertThrows(RecommendationNotFoundException.class,
                () ->  recipeRecommendationService.getRecommendationsByUser(userId));
    }

    @Test
    @DisplayName("returns recipe recommendations when fridge contains ingredients")
    void shouldReturnRecommendations_whenFridgeContainsIngredients() {
        // given
        Long userId = 1L;
        given(fridgeQueryService.getFridgeByUserId(userId)).willReturn(fridge);

        String url = "http://grocey-ai:5001/api/recommend/recipes/fridge/" + userId;
        given(restTemplate.getForEntity(url, List.class))
                .willReturn(new ResponseEntity<>(List.of(101L), HttpStatus.OK));

        ReflectionTestUtils.setField(recipe, "id", 101L);
        given(recipeQueryService.getRecipes(List.of(101L))).willReturn(List.of(recipe));
        given(recipeRecommendationRepository.saveAll(anyList()))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        List<RecipeRecommendationResponse> result = recipeRecommendationService.getRecommendationsByFridge(userId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).recipeName()).isEqualTo("Kimchi Fried Rice");
    }

    @Test
    @DisplayName("throws RecommendationNotFoundException when no fridge-based recommendations are found")
    void shouldThrowException_whenFridgeRecommendationsNotFound() {
        given(fridgeQueryService.getFridgeByUserId(1L)).willReturn(fridge);

        String url = "http://grocey-ai:5001/api/recommend/recipes/fridge/" + user.getId();
        given(restTemplate.getForEntity(url, List.class))
                .willReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

        // when & then
        assertThrows(RecommendationNotFoundException.class,
                () ->  recipeRecommendationService.getRecommendationsByFridge(1L));
    }
}