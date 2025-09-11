package com.hyewon.grocey_api.domain.fridge.dto;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;

public record FridgeResponse(
        Double fridgeTemperature,
        Double freezerTemperature
) {
    public static FridgeResponse from(Fridge fridge) {
        return new FridgeResponse(
                fridge.getFridgeTemperature(),
                fridge.getFreezerTemperature()
        );
    }
}
