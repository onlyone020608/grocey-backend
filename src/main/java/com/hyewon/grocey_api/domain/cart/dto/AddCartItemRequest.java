package com.hyewon.grocey_api.domain.cart.dto;

import lombok.Getter;

@Getter
public class AddCartItemRequest {
    private Long productId;
    private int quantity;

    public AddCartItemRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
