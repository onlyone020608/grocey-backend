package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.entity.PaymentMethod;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;

import java.util.List;

public record OrderRequest(
        List<Long> cartItemIds,
        String address,
        String paymentMethod
) {
    public PaymentMethod toPaymentMethod() {
        try {
            return PaymentMethod.valueOf(paymentMethod.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid payment method: " + paymentMethod);
        }
    }
}
