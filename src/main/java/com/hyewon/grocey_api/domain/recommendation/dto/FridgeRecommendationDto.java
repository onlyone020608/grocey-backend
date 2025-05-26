package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.FridgeRecommendation;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FridgeRecommendationDto {
    private Long recommendationId;
    private LocalDateTime createdAt;
    private List<FridgeRecommendedProductDto> products;

    public FridgeRecommendationDto(Long recommendationId, LocalDateTime createdAt, List<FridgeRecommendedProductDto> products) {
        this.recommendationId = recommendationId;
        this.createdAt = createdAt;
        this.products = products;
    }

    public FridgeRecommendationDto(FridgeRecommendation recommendation) {
        this.recommendationId = recommendation.getId();
        this.createdAt = recommendation.getCreatedAt();
        this.products = recommendation.getRecommendedProducts().stream()
                .map(FridgeRecommendedProductDto::new)
                .collect(Collectors.toList());
    }
}
