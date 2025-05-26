package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.PRODUCT_NOT_FOUND;

    public ProductNotFoundException(Long productId) {
        super("Product not found with id: " + productId);
    }
}
