package com.hyewon.grocey_api.domain.cart.dto;

import lombok.Getter;

@Getter
public class CartItemResponseDto {
    private Long productId;
    private Long cartItemId;
    private String productName;
    private String imageUrl;
    private double price;
    private int quantity;

    public CartItemResponseDto(Long cartItemId, Long productId, String productName, String imageUrl, double price, int quantity) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }
}
