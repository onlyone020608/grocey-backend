package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.recommendation.dto.RecipeRecommendationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/recipes/recommendations")
@RestController
@RequiredArgsConstructor
public class RecipeRecommendationController {

    private final RecipeRecommendationService recommendationService;

    @GetMapping("/personal/{userId}")
    public List<RecipeRecommendationDto> recommendByUser(@PathVariable Long userId) {
        return recommendationService.getRecommendationsByUser(userId);
    }

    @GetMapping("/fridge/{fridgeId}")
    public List<RecipeRecommendationDto> recommendByFridge(@PathVariable Long fridgeId) {
        return recommendationService.getRecommendationsByFridge(fridgeId);
    }



}
