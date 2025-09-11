package com.hyewon.grocey_api.domain.cart.dto;

public record AddCartItemRequest(
        Long productId,
        int quantity
) {}
