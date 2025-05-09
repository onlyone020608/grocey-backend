package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.Order;
import com.hyewon.grocey_api.domain.order.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderSummaryDto {
    private Long orderId;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;
    private final List<OrderItemDto> items;

    public OrderSummaryDto(Order order) {
        this.orderId = order.getId();
        this.createdAt = order.getCreatedAt();
        this.orderStatus = order.getOrderStatus();
        this.items = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .toList();
    }



}
