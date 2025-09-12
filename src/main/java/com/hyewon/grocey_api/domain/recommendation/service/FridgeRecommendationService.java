package com.hyewon.grocey_api.domain.recommendation.service;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.service.FridgeIngredientManager;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.service.ProductQueryService;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationResponse;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;
import com.hyewon.grocey_api.domain.recommendation.repository.FridgeRecommendationRepository;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FridgeRecommendationService {
    private final FridgeRecommendationRepository fridgeRecommendationRepository;
    private final FridgeQueryService fridgeQueryService;
    private final ProductQueryService productQueryService;
    private final RestTemplate restTemplate;
    private final FridgeIngredientManager fridgeIngredientManager;

    @Transactional
    @CachePut(value = "fridgeRecommendations", key = "#userId")
    public FridgeRecommendationResponse getLatestRecommendation(Long userId) {
        Fridge fridge = fridgeQueryService.getFridgeByUserId(userId);

        List<Long> ingredientIds = fetchRecommendedIngredientIds(userId);
        if (ingredientIds.isEmpty()) {
            throw RecommendationNotFoundException.forFridgeProduct(fridge.getId());
        }

        List<Product> products =  productQueryService.findRandomOnePerIngredient(ingredientIds);

        FridgeRecommendation recommendation = fridgeRecommendationRepository.save(FridgeRecommendation.of(fridge));
        products.forEach(recommendation::addRecommendedProduct);

        return FridgeRecommendationResponse.from(recommendation);
    }

    public List<Long> fetchRecommendedIngredientIds(Long userId) {
        String url = "http://grocey-ai:5001/api/recommend/" + userId;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody(); // [1, 2, 3]
    }

    public void simulateFridgeChange(Long userId) {
        Fridge fridge = fridgeQueryService.getFridgeByUserId(userId);

        List<FridgeIngredient> ingredients = fridgeIngredientManager.getByFridgeId(fridge.getId());

        if (ingredients.size() <= 2) return;

        Collections.shuffle(ingredients);
        List<FridgeIngredient> toRemove = ingredients.subList(0, 2);
        fridgeIngredientManager.deleteAll(toRemove);
    }
}
