package com.hyewon.grocey_api.domain.order.dto;

import com.hyewon.grocey_api.domain.order.PaymentMethod;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    private List<Long> cartItemIds;
    private String address;
    private String paymentMethod;

    public PaymentMethod toPaymentMethod() {
        try {
            return PaymentMethod.valueOf(paymentMethod.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid payment method: " + paymentMethod);
        }
    }
}
