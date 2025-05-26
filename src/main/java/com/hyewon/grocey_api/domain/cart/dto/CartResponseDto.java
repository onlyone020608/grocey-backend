package com.hyewon.grocey_api.domain.cart.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CartResponseDto {
    private long cartId;
    private List<CartItemResponseDto> items;

    public CartResponseDto(long cartId, List<CartItemResponseDto> items) {
        this.cartId = cartId;
        this.items = items;
    }
}
