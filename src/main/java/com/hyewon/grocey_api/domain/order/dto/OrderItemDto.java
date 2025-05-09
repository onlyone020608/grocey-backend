package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemDto {
    private final Long orderItemId;
    private final String productName;
    private final int quantity;
    private final int price;

    public OrderItemDto(OrderItem item) {
        this.orderItemId = item.getId();
        this.productName = item.getProduct().getProductName(); // 필요에 따라 수정
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
    }
}
