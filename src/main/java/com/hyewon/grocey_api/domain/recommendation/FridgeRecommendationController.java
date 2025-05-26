package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationDto;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendedProductDto;
import com.hyewon.grocey_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations/fridge")
@RequiredArgsConstructor
public class FridgeRecommendationController {

    private final FridgeRecommendationService fridgeRecommendationService;

    @GetMapping
    public FridgeRecommendationDto getFridgeRecommendation(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long fridgeId = userDetails.getUser().getFridge().getId();
        fridgeRecommendationService.simulateFridgeChange(fridgeId);
        return fridgeRecommendationService.getLatestRecommendation(userDetails.getUser().getFridge().getId());
    }
}
