package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class OrderNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.ORDER_NOT_FOUND;

    public OrderNotFoundException(Long orderId) {
        super("Order not found (id=" + orderId + ")");
    }
}
