package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.cart.dto.CartItemResponseDto;
import com.hyewon.grocey_api.domain.cart.dto.CartResponseDto;
import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import com.hyewon.grocey_api.domain.recipe.RecipeRepository;
import com.hyewon.grocey_api.domain.recommendation.dto.RecipeRecommendationDto;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeRecommendationService {

    private final RecipeRecommendationRepository recipeRecommendationRepository;
    private final UserRepository userRepository;
    private final FridgeRepository fridgeRepository;
    private final RecipeRepository recipeRepository;


    public List<RecipeRecommendationDto> getRecommendationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Long> recipeIds = fetchPreferenceBasedRecipeIds(userId); // AI 호출
        if (recipeIds.isEmpty()) {
            throw RecommendationNotFoundException.forUserRecipe(userId);
        }

        List<Recipe> recipes = recipeRepository.findAllById(recipeIds);

        List<RecipeRecommendation> saved = recipes.stream()
                .map(recipe -> new RecipeRecommendation(user, recipe, RecommendationType.PREFERENCE_BASED))
                .toList();
        recipeRecommendationRepository.saveAll(saved);

        return saved.stream()
                .map(RecipeRecommendationDto::new)
                .toList();

    }

    public List<RecipeRecommendationDto> getRecommendationsByFridge(Long fridgeId) {
        Fridge fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new FridgeNotFoundException(fridgeId));

        Long userId = fridge.getUsers().stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(fridgeId))
                .getId();

        List<Long> recipeIds = fetchFridgeBasedRecipeIds(userId);
        if (recipeIds.isEmpty()) {
            throw RecommendationNotFoundException.forFridgeRecipe(fridgeId);
        }

        List<Recipe> recipes = recipeRepository.findAllById(recipeIds);

        List<RecipeRecommendation> saved = recipes.stream()
                .map(recipe -> new RecipeRecommendation(fridge, recipe, RecommendationType.FRIDGE_BASED))
                .toList();
        recipeRecommendationRepository.saveAll(saved);

        return saved.stream()
                .map(RecipeRecommendationDto::new)
                .toList();
    }

    private List<Long> fetchFridgeBasedRecipeIds(Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:5001/api/recommend/recipes/fridge/" + userId;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }

    private List<Long> fetchPreferenceBasedRecipeIds(Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:5001/api/recommend/recipes/preference/" + userId;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }


}
