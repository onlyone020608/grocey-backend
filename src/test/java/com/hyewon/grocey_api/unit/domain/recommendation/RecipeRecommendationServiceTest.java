package com.hyewon.grocey_api.unit.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.service.RecipeQueryService;
import com.hyewon.grocey_api.domain.recommendation.dto.RecipeRecommendationResponse;
import com.hyewon.grocey_api.domain.recommendation.repository.RecipeRecommendationRepository;
import com.hyewon.grocey_api.domain.recommendation.service.RecipeRecommendationService;
import com.hyewon.grocey_api.domain.user.entity.AgeGroup;
import com.hyewon.grocey_api.domain.user.entity.Gender;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecipeRecommendationServiceTest {

    @Mock
    private RecipeRecommendationRepository recipeRecommendationRepository;
    @Mock private UserQueryService userQueryService;
    @Mock
    private RestTemplate restTemplate;
    @Mock private FridgeQueryService fridgeQueryService;
    @Mock private RecipeQueryService recipeQueryService;
    @InjectMocks
    private RecipeRecommendationService recipeRecommendationService;

    private User user;
    private Fridge fridge;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        user = new User("tester", "test@email.com", "pw", AgeGroup.TWENTIES, Gender.FEMALE);
        ReflectionTestUtils.setField(user, "id", 1L);

        fridge = new Fridge(4.0, -18.0);
        ReflectionTestUtils.setField(fridge, "id", 2L);
        user.assignFridge(fridge);

        recipe = new Recipe("Bibimbap", "step1\nstep2", 20, 2);
        ReflectionTestUtils.setField(recipe, "imageUrl", "bibimbap.jpg");
    }

    @Test
    @DisplayName("getRecommendationsByUser - returns recipe recommendations")
    void getRecommendationsByUser_returnsRecommendations_whenAiReturnsRecipeIds() {
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
    @DisplayName("getRecommendationsByUser - throws when no recommendations")
    void getRecommendationsByUser_shouldThrowIfEmpty() {
        // given
        Long userId = 1L;
        given(userQueryService.getUserById(userId)).willReturn(user);
        String url = "http://grocey-ai:5001/api/recommend/recipes/preference/" + userId;
        given(restTemplate.getForEntity(url, List.class))
                .willReturn(new ResponseEntity<>(List.of(), HttpStatus.OK)); // 빈 리스트

        // when & then
        assertThatThrownBy(() -> recipeRecommendationService.getRecommendationsByUser(userId))
                .isInstanceOf(RecommendationNotFoundException.class);
    }

    @Test
    @DisplayName("getRecommendationsByFridge - returns recipe recommendations")
    void getRecommendationsByFridge_shouldReturnList() {
        // given
        Long fridgeId = 2L;
        Long userId = 1L;
        given(fridgeQueryService.getFridge(fridgeId)).willReturn(fridge);

        // AI 응답 mock
        String url = "http://grocey-ai:5001/api/recommend/recipes/fridge/" + userId;
        given(restTemplate.getForEntity(url, List.class))
                .willReturn(new ResponseEntity<>(List.of(101L), HttpStatus.OK));

        // recipe mock
        ReflectionTestUtils.setField(recipe, "id", 101L);
        given(recipeQueryService.getRecipes(List.of(101L))).willReturn(List.of(recipe));

        // saveAll mock
        given(recipeRecommendationRepository.saveAll(anyList()))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        List<RecipeRecommendationResponse> result = recipeRecommendationService.getRecommendationsByFridge(fridgeId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRecipeName()).isEqualTo("Bibimbap");
    }

    @Test
    @DisplayName("getRecommendationsByFridge - throws when no recommendations")
    void getRecommendationsByFridge_shouldThrowIfEmpty() {
        given(fridgeQueryService.getFridge(2L)).willReturn(fridge);

        String url = "http://grocey-ai:5001/api/recommend/recipes/fridge/" + user.getId();
        given(restTemplate.getForEntity(url, List.class))
                .willReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

        // when & then
        assertThatThrownBy(() -> recipeRecommendationService.getRecommendationsByFridge(2L))
                .isInstanceOf(RecommendationNotFoundException.class);
    }




}