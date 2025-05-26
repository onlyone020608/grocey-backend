package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.Gender;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GenderUpdateRequest {
    private String gender;

    public Gender toEnum() {
        try {
            return Gender.valueOf(gender.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid gender value: " + gender);
        }
    }

    @Builder
    public GenderUpdateRequest(String gender) {
        this.gender = gender;
    }

    public GenderUpdateRequest() {
    }
}
