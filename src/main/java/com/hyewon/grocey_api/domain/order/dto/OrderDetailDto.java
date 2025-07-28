package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.entity.OrderStatus;
import com.hyewon.grocey_api.domain.order.entity.PaymentMethod;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderDetailDto {
    private Long orderId;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private String shippingAddress;
    private List<OrderItemDto> items;

    public OrderDetailDto(Order order) {
        this.orderId = order.getId();
        this.createdAt = order.getCreatedAt();
        this.orderStatus = order.getOrderStatus();
        this.paymentMethod = order.getPaymentMethod();
        this.shippingAddress = order.getAddress();
        this.items = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .toList();
    }
}
