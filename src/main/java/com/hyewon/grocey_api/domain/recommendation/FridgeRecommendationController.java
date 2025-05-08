package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationDto;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendedProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations/fridge")
@RequiredArgsConstructor
public class FridgeRecommendationController {

    private final FridgeRecommendationService fridgeRecommendationService;

    @GetMapping("/{fridgeId}")
    public FridgeRecommendationDto getFridgeRecommendation(@PathVariable Long fridgeId) {
        return fridgeRecommendationService.getLatestRecommendation(fridgeId);
    }
}
