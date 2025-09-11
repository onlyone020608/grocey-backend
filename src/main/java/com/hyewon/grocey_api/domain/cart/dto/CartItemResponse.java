package com.hyewon.grocey_api.domain.cart.dto;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        String productName,
        String imageUrl,
        double price,
        int quantity
) {
    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getImageUrl(),
                item.getProduct().getPrice(),
                item.getQuantity()
        );
    }
}
