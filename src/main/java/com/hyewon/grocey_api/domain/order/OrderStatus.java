package com.hyewon.grocey_api.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("결제전"),
    CONFIRMED("주문확정"),
    DELIVERING("배송중"),
    COMPLETED("배송완료"),
    CANCELED("주문취소");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

}

