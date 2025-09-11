package com.hyewon.grocey_api.domain.cart.dto;

import java.util.List;

public record CartResponse(
        long cartId,
        List<CartItemResponse> items
) {}
