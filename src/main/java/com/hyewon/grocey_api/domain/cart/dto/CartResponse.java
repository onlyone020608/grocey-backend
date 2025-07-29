package com.hyewon.grocey_api.domain.cart.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CartResponse {
    private long cartId;
    private List<CartItemResponse> items;

    public CartResponse(long cartId, List<CartItemResponse> items) {
        this.cartId = cartId;
        this.items = items;
    }
}
