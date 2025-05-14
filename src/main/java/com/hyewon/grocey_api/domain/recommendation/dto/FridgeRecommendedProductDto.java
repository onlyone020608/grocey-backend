package com.hyewon.grocey_api.domain.recommendation.dto;

import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.recommendation.FridgeRecommendedProduct;
import lombok.Getter;

@Getter
public class FridgeRecommendedProductDto {

    private final Long productId;
    private final String productName;
    private final String brand;
    private final int price;
    private final String imageUrl;

    public FridgeRecommendedProductDto(FridgeRecommendedProduct recommendedProduct) {
        this.productId = recommendedProduct.getProduct().getId();
        this.productName = recommendedProduct.getProduct().getProductName();
        this.brand = recommendedProduct.getProduct().getBrandName();
        this.price = recommendedProduct.getProduct().getPrice();
        this.imageUrl = recommendedProduct.getProduct().getImageUrl();
    }
}
