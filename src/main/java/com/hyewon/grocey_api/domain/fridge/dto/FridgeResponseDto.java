package com.hyewon.grocey_api.domain.fridge.dto;

import lombok.Getter;

@Getter
public class FridgeResponseDto {
    private Double fridgeTemperature;
    private Double freezerTemperature;

    public FridgeResponseDto(Double fridgeTemperature, Double freezerTemperature) {
        this.fridgeTemperature = fridgeTemperature;
        this.freezerTemperature = freezerTemperature;
    }
}
