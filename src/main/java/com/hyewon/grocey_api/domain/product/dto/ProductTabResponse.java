package com.hyewon.grocey_api.domain.product.dto;

import java.util.List;

public record ProductTabResponse(
        List<ProductResponse> products
) {
    public static ProductTabResponse from(List<ProductResponse> products) {
        return new ProductTabResponse(products);
    }
}
