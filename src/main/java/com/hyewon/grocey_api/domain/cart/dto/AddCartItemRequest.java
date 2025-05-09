package com.hyewon.grocey_api.domain.cart.dto;

import lombok.Getter;

@Getter
public class AddCartItemRequest {
    private Long userId;
    private Long productId;
    private int quantity;

}
