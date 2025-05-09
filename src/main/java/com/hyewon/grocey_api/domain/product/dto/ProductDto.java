package com.hyewon.grocey_api.domain.product.dto;

import com.hyewon.grocey_api.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductDto {
    private Long productId;
    private String brandName;
    private String productName;
    private int price;
    private String imageUrl;

    public ProductDto(Product product) {
        this.productId = product.getId();
        this.brandName = product.getBrandName();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }


}
