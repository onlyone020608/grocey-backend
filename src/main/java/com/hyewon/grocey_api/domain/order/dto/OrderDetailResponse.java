package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.entity.OrderStatus;
import com.hyewon.grocey_api.domain.order.entity.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long orderId,
        LocalDateTime createdAt,
        OrderStatus orderStatus,
        PaymentMethod paymentMethod,
        String shippingAddress,
        List<OrderItemResponse> items
) {
    public static OrderDetailResponse from(Order order) {
        return new OrderDetailResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getOrderStatus(),
                order.getPaymentMethod(),
                order.getAddress(),
                order.getOrderItems().stream()
                        .map(OrderItemResponse::from)
                        .toList()
        );
    }
}
