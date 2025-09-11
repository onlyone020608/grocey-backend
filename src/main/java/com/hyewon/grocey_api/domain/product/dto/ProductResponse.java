package com.hyewon.grocey_api.domain.product.dto;

import com.hyewon.grocey_api.domain.product.entity.Product;

public record ProductResponse(
        Long productId,
        String brandName,
        String productName,
        double price,
        String imageUrl
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getBrand(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}
