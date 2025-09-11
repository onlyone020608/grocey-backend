package com.hyewon.grocey_api.domain.cart.dto;

public record UpdateCartItemRequest(
        Long cartItemId,
        int quantity
) {}