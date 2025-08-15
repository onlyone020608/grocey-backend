package com.hyewon.grocey_api.domain.product.dto;

import com.hyewon.grocey_api.domain.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse {
    private Long productId;
    private String brandName;
    private String productName;
    private double price;
    private String imageUrl;

    public ProductResponse(Product product) {
        this.productId = product.getId();
        this.brandName = product.getBrand();
        this.productName = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }
}
