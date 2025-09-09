package com.hyewon.grocey_api.domain.fridge.dto;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import lombok.Getter;

@Getter
public class FridgeResponse {
    private Double fridgeTemperature;
    private Double freezerTemperature;

    public FridgeResponse(Double fridgeTemperature, Double freezerTemperature) {
        this.fridgeTemperature = fridgeTemperature;
        this.freezerTemperature = freezerTemperature;
    }

    public static FridgeResponse from(Fridge fridge) {
        return new FridgeResponse(
                fridge.getFridgeTemperature(),
                fridge.getFreezerTemperature()
        );
    }
}
