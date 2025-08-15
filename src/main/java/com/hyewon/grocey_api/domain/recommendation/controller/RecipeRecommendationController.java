package com.hyewon.grocey_api.domain.recommendation.controller;

import com.hyewon.grocey_api.domain.recommendation.dto.RecipeRecommendationResponse;
import com.hyewon.grocey_api.domain.auth.security.CustomUserDetails;
import com.hyewon.grocey_api.domain.recommendation.service.RecipeRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/recipes/recommendations")
@RestController
@RequiredArgsConstructor
public class RecipeRecommendationController {
    private final RecipeRecommendationService recommendationService;

    @GetMapping("/personal")
    public List<RecipeRecommendationResponse> recommendByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return recommendationService.getRecommendationsByUser(userDetails.getId());
    }

    @GetMapping("/fridge")
    public List<RecipeRecommendationResponse> recommendByFridge(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return recommendationService.getRecommendationsByFridge(userDetails.getUser().getFridge().getId());
    }
}
