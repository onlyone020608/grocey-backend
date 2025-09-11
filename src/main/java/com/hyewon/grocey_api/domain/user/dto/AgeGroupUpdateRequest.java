package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.entity.AgeGroup;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;

import java.util.Arrays;

public record AgeGroupUpdateRequest(
        int ageValue
) {
    public AgeGroup toEnum() {
        return Arrays.stream(AgeGroup.values())
                .filter(g -> g.getValue() == ageValue)
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("Invalid age group value: " + ageValue));
    }
}
