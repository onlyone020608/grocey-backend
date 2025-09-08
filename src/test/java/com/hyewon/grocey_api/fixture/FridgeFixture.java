package com.hyewon.grocey_api.fixture;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;

public class FridgeFixture {

    public static Fridge aFridge() {
        return Fridge.builder()
                .id(1L)
                .fridgeTemperature(4.0)
                .freezerTemperature(-18.0)
                .build();
    }
}
