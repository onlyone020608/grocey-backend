package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {
    private final Long orderItemId;
    private final String productName;
    private final int quantity;
    private final double price;
    private final String imageUrl;

    public OrderItemResponse(OrderItem item) {
        this.orderItemId = item.getId();
        this.productName = item.getProduct().getName();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
        this.imageUrl = item.getProduct().getImageUrl();
    }
}
