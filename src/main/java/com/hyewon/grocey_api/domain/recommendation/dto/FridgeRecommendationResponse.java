package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FridgeRecommendationResponse {
    private Long recommendationId;
    private LocalDateTime createdAt;
    private List<FridgeRecommendedProductResponse> products;

    private FridgeRecommendationResponse(Long recommendationId, LocalDateTime createdAt, List<FridgeRecommendedProductResponse> products) {
        this.recommendationId = recommendationId;
        this.createdAt = createdAt;
        this.products = products;
    }

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
