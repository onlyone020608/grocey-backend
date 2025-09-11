package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendedProduct;

public record FridgeRecommendedProductResponse(
        Long productId,
        String productName,
        String brand,
        double price,
        String imageUrl
) {
    public static FridgeRecommendedProductResponse from(FridgeRecommendedProduct recommendedProduct) {
        return new FridgeRecommendedProductResponse(
                recommendedProduct.getProduct().getId(),
                recommendedProduct.getProduct().getName(),
                recommendedProduct.getProduct().getBrand(),
                recommendedProduct.getProduct().getPrice(),
                recommendedProduct.getProduct().getImageUrl()
        );
    }
}
