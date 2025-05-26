package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemDto {
    private final Long orderItemId;
    private final String productName;
    private final int quantity;
    private final double price;
    private final String imageUrl;

    public OrderItemDto(OrderItem item) {
        this.orderItemId = item.getId();
        this.productName = item.getProduct().getProductName(); // 필요에 따라 수정
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
        this.imageUrl = item.getProduct().getImageUrl();
    }
}
