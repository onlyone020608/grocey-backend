package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.entity.OrderItem;

public record OrderItemResponse(
        Long orderItemId,
        String productName,
        int quantity,
        double price,
        String imageUrl
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPrice(),
                item.getProduct().getImageUrl()
        );
    }
}
