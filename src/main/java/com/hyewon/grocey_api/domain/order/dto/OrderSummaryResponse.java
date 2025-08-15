package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderSummaryResponse {
    private Long orderId;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;
    private final List<OrderItemResponse> items;

    public OrderSummaryResponse(Order order) {
        this.orderId = order.getId();
        this.createdAt = order.getCreatedAt();
        this.orderStatus = order.getOrderStatus();
        this.items = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .toList();
    }
}
