package com.hyewon.grocey_api.domain.cart.dto;

import lombok.Getter;

@Getter
public class UpdateCartItemRequest {
    private Long cartItemId;
    private int quantity;
}
