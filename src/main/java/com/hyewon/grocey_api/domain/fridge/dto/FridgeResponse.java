package com.hyewon.grocey_api.domain.fridge.dto;

import lombok.Getter;

@Getter
public class FridgeResponse {
    private Double fridgeTemperature;
    private Double freezerTemperature;

    public FridgeResponse(Double fridgeTemperature, Double freezerTemperature) {
        this.fridgeTemperature = fridgeTemperature;
        this.freezerTemperature = freezerTemperature;
    }
}
