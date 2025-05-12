package com.hyewon.grocey_api.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    DELIVERING("Delivering"),
    COMPLETED("Completed"),
    CANCELED("Canceled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

}

