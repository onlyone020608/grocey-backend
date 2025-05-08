package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendationDto;
import com.hyewon.grocey_api.domain.recommendation.dto.FridgeRecommendedProductDto;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FridgeRecommendationService {
    private final FridgeRecommendationRepository fridgeRecommendationRepository;

    public FridgeRecommendationDto getLatestRecommendation(Long fridgeId) {
        FridgeRecommendation recommendation = fridgeRecommendationRepository
                .findTopByFridgeIdOrderByCreatedAtDesc(fridgeId)
                .orElseThrow(() -> new RecommendationNotFoundException(fridgeId));

        return new FridgeRecommendationDto(recommendation);
    }
}
