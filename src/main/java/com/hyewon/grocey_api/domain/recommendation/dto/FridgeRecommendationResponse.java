package com.hyewon.grocey_api.domain.recommendation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;

import java.time.LocalDateTime;
import java.util.List;

public record FridgeRecommendationResponse(
        Long recommendationId,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
