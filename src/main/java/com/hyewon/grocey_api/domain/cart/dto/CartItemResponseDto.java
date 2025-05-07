package com.hyewon.grocey_api.domain.cart.dto;

import lombok.Getter;

@Getter
public class CartItemResponseDto {
    private Long productId;
    private String productName;
    private String imageUrl;
    private int price;
    private int quantity;

    public CartItemResponseDto(Long productId, String productName, String imageUrl, int price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }
}
