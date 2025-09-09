package com.hyewon.grocey_api.domain.recommendation.controller;

import com.hyewon.grocey_api.domain.auth.security.CustomUserDetails;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationResponse;
import com.hyewon.grocey_api.domain.recommendation.service.FridgeRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations/fridge")
@RequiredArgsConstructor
public class FridgeRecommendationController {
    private final FridgeRecommendationService fridgeRecommendationService;

    @GetMapping
    public FridgeRecommendationResponse getFridgeRecommendation(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        fridgeRecommendationService.simulateFridgeChange(userId);
        return fridgeRecommendationService.getLatestRecommendation(userDetails.getId());
    }
}
