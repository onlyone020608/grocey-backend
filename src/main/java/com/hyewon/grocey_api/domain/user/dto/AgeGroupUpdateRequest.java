package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.AgeGroup;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class AgeGroupUpdateRequest {
    private int ageValue;

    public AgeGroup toEnum() {
        return Arrays.stream(AgeGroup.values())
                .filter(g -> g.getValue() == ageValue)
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("Invalid age group value: " + ageValue));
    }

    @Builder
    public AgeGroupUpdateRequest(int ageValue) {
        this.ageValue = ageValue;
    }

    public AgeGroupUpdateRequest() {
    }
}
