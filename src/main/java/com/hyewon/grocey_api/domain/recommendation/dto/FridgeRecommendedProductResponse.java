package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendedProduct;
import lombok.Getter;

@Getter
public class FridgeRecommendedProductResponse {

    private final Long productId;
    private final String productName;
    private final String brand;
    private final double price;
    private final String imageUrl;

    private FridgeRecommendedProductResponse(Long productId, String productName, String brand,
                                             double price, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
    }

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
