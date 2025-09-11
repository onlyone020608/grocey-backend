package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;

import java.time.LocalDateTime;
import java.util.List;

public record FridgeRecommendationResponse(
        Long recommendationId,
        LocalDateTime createdAt,
        List<FridgeRecommendedProductResponse> products
) {
    public static FridgeRecommendationResponse from(FridgeRecommendation recommendation) {
        return new FridgeRecommendationResponse(
                recommendation.getId(),
                recommendation.getCreatedAt(),
                recommendation.getRecommendedProducts().stream()
                        .map(FridgeRecommendedProductResponse::from)
                        .toList()
        );
    }
}
