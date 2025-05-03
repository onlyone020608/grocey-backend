package com.hyewon.grocey_api.domain.user;

import lombok.Getter;

@Getter
public enum AgeGroup {
    TEENS(10, "10대"),
    TWENTIES(20, "20대"),
    THIRTIES(30, "30대"),
    FORTIES(40, "40대"),
    FIFTIES(50, "50대"),
    SIXTIES(60, "60대"),
    OVER_SIXTIES(70, "60대 이상");

    private final int value;
    private final String label;

    AgeGroup(int value, String label) {
        this.value = value;
        this.label = label;
    }






}
