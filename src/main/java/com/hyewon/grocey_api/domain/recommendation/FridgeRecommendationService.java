package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.product.ProductRepository;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationDto;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
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
    private final FridgeRepository fridgeRepository;
    private final ProductRepository productRepository;
    private final FridgeRecommendedProductRepository fridgeRecommendedProductRepository;
    private final FridgeIngredientRepository  fridgeIngredientRepository;


    @Transactional
    public FridgeRecommendationDto getLatestRecommendation(Long fridgeId) {
        Fridge fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new FridgeNotFoundException(fridgeId));

        Long userId = fridge.getUsers().stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(fridgeId))
                .getId();

        List<Long> ingredientIds = fetchRecommendedIngredientIds(userId);
        if (ingredientIds.isEmpty()) {
            throw RecommendationNotFoundException.forFridgeProduct(fridgeId);
        }


        List<Product> products = productRepository.findRandomOneEachByIngredient(ingredientIds);


        FridgeRecommendation recommendation = fridgeRecommendationRepository.save(new FridgeRecommendation(fridge));
        List<FridgeRecommendedProduct> savedProducts = products.stream()
                .map(product -> new FridgeRecommendedProduct(product, recommendation))
                .toList();

        fridgeRecommendedProductRepository.saveAll(savedProducts);
        recommendation.getRecommendedProducts().addAll(savedProducts);


        return new FridgeRecommendationDto(recommendation);
    }

    public List<Long> fetchRecommendedIngredientIds(Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://grocey-ai:5001/api/recommend/" + userId;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody(); // [1, 2, 3]
    }

    public void simulateFridgeChange(Long fridgeId) {
        List<FridgeIngredient> ingredients = fridgeIngredientRepository.findByFridgeId(fridgeId);

        if (ingredients.size() <= 2) return;

        Collections.shuffle(ingredients);
        List<FridgeIngredient> toRemove = ingredients.subList(0, 2);
        fridgeIngredientRepository.deleteAll(toRemove);
    }
}
