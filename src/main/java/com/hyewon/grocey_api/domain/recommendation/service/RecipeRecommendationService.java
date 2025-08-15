package com.hyewon.grocey_api.domain.recommendation.service;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.service.RecipeQueryService;
import com.hyewon.grocey_api.domain.recommendation.entity.RecipeRecommendation;
import com.hyewon.grocey_api.domain.recommendation.repository.RecipeRecommendationRepository;
import com.hyewon.grocey_api.domain.recommendation.dto.RecipeRecommendationResponse;
import com.hyewon.grocey_api.domain.recommendation.entity.RecommendationType;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeRecommendationService {
    private final RecipeRecommendationRepository recipeRecommendationRepository;
    private final UserQueryService userQueryService;
    private final RestTemplate restTemplate;
    private final FridgeQueryService fridgeQueryService;
    private final RecipeQueryService recipeQueryService;

    @Transactional
    public List<RecipeRecommendationResponse> getRecommendationsByUser(Long userId) {
        User user = userQueryService.getUserById(userId);

        List<Long> recipeIds = fetchPreferenceBasedRecipeIds(userId); // AI 호출
        if (recipeIds.isEmpty()) {
            throw RecommendationNotFoundException.forUserRecipe(userId);
        }

        List<Recipe> recipes = recipeQueryService.getRecipes(recipeIds);

        List<RecipeRecommendation> saved = recipes.stream()
                .map(recipe -> RecipeRecommendation.ofUser(user, recipe, RecommendationType.PREFERENCE_BASED))
                .toList();
        recipeRecommendationRepository.saveAll(saved);

        return saved.stream()
                .map(RecipeRecommendationResponse::new)
                .toList();
    }

    @Transactional
    public List<RecipeRecommendationResponse> getRecommendationsByFridge(Long fridgeId) {
        Fridge fridge = fridgeQueryService.getFridge(fridgeId);

        Long userId = fridge.getUsers().stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(fridgeId))
                .getId();

        List<Long> recipeIds = fetchFridgeBasedRecipeIds(userId);

        if (recipeIds.isEmpty()) {
            throw RecommendationNotFoundException.forFridgeRecipe(fridgeId);
        }

        List<Recipe> recipes = recipeQueryService.getRecipes(recipeIds);

        List<RecipeRecommendation> saved = recipes.stream()
                .map(recipe -> RecipeRecommendation.ofFridge(fridge, recipe, RecommendationType.FRIDGE_BASED))
                .toList();
        recipeRecommendationRepository.saveAll(saved);

        return saved.stream()
                .map(RecipeRecommendationResponse::new)
                .toList();
    }

    private List<Long> fetchFridgeBasedRecipeIds(Long userId) {
        String url = "http://grocey-ai:5001/api/recommend/recipes/fridge/" + userId;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }

    private List<Long> fetchPreferenceBasedRecipeIds(Long userId) {
        String url = "http://grocey-ai:5001/api/recommend/recipes/preference/" + userId;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }
}
