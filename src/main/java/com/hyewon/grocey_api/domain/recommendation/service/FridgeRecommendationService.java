package com.hyewon.grocey_api.domain.recommendation.service;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.service.FridgeIngredientManager;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.service.ProductQueryService;
import com.hyewon.grocey_api.domain.recommendation.repository.FridgeRecommendationRepository;
import com.hyewon.grocey_api.domain.recommendation.repository.FridgeRecommendedProductRepository;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationResponse;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendedProduct;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final FridgeRecommendedProductRepository fridgeRecommendedProductRepository;
    private final FridgeIngredientManager fridgeIngredientManager;


    @Transactional
    public FridgeRecommendationResponse getLatestRecommendation(Long fridgeId) {
        Fridge fridge = fridgeQueryService.getFridge(fridgeId);

        Long userId = fridge.getUsers().stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(fridgeId))
                .getId();

        List<Long> ingredientIds = fetchRecommendedIngredientIds(userId);
        if (ingredientIds.isEmpty()) {
            throw RecommendationNotFoundException.forFridgeProduct(fridgeId);
        }


        List<Product> products =  productQueryService.findRandomOnePerIngredient(ingredientIds);


        FridgeRecommendation recommendation = fridgeRecommendationRepository.save(new FridgeRecommendation(fridge));
        List<FridgeRecommendedProduct> savedProducts = products.stream()
                .map(product -> new FridgeRecommendedProduct(product, recommendation))
                .toList();

        fridgeRecommendedProductRepository.saveAll(savedProducts);
        recommendation.getRecommendedProducts().addAll(savedProducts);


        return new FridgeRecommendationResponse(recommendation);
    }

    public List<Long> fetchRecommendedIngredientIds(Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://grocey-ai:5001/api/recommend/" + userId;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody(); // [1, 2, 3]
    }

    public void simulateFridgeChange(Long fridgeId) {
        List<FridgeIngredient> ingredients = fridgeIngredientManager.getByFridgeId(fridgeId);

        if (ingredients.size() <= 2) return;

        Collections.shuffle(ingredients);
        List<FridgeIngredient> toRemove = ingredients.subList(0, 2);
        fridgeIngredientManager.deleteAll(toRemove);

    }
}
