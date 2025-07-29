package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FridgeRecommendationResponse {
    private Long recommendationId;
    private LocalDateTime createdAt;
    private List<FridgeRecommendedProductResponse> products;

    public FridgeRecommendationResponse(Long recommendationId, LocalDateTime createdAt, List<FridgeRecommendedProductResponse> products) {
        this.recommendationId = recommendationId;
        this.createdAt = createdAt;
        this.products = products;
    }

    public FridgeRecommendationResponse(FridgeRecommendation recommendation) {
        this.recommendationId = recommendation.getId();
        this.createdAt = recommendation.getCreatedAt();
        this.products = recommendation.getRecommendedProducts().stream()
                .map(FridgeRecommendedProductResponse::new)
                .collect(Collectors.toList());
    }
}
