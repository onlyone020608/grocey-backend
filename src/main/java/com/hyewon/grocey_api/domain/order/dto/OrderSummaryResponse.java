package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderSummaryResponse(
        Long orderId,
        LocalDateTime createdAt,
        OrderStatus orderStatus,
        List<OrderItemResponse> items
) {
    public static OrderSummaryResponse from(Order order) {
        return new OrderSummaryResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getOrderStatus(),
                order.getOrderItems().stream()
                        .map(OrderItemResponse::from)
                        .toList()
        );
    }
}