package com.hyewon.grocey_api.domain.recommendation.controller;

import com.hyewon.grocey_api.domain.recommendation.service.FridgeRecommendationService;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationResponse;
import com.hyewon.grocey_api.domain.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations/fridge")
@RequiredArgsConstructor
public class FridgeRecommendationController {

    private final FridgeRecommendationService fridgeRecommendationService;

    @GetMapping
    public FridgeRecommendationResponse getFridgeRecommendation(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long fridgeId = userDetails.getUser().getFridge().getId();
        fridgeRecommendationService.simulateFridgeChange(fridgeId);
        return fridgeRecommendationService.getLatestRecommendation(userDetails.getUser().getFridge().getId());
    }
}
