package com.hyewon.grocey_api.domain.cart.dto;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import lombok.Getter;

@Getter
public class CartItemResponse {
    private Long productId;
    private Long cartItemId;
    private String productName;
    private String imageUrl;
    private double price;
    private int quantity;

    public CartItemResponse(Long cartItemId, Long productId, String productName, String imageUrl, double price, int quantity) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

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
