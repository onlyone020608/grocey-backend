package com.hyewon.grocey_api.global.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super("Order not found for orderId = " + orderId);
    }
}
