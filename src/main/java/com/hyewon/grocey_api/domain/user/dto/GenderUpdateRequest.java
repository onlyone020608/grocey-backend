package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.entity.Gender;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import lombok.Builder;

@Builder
public record GenderUpdateRequest(
        String gender
) {
    public Gender toEnum() {
        try {
            return Gender.valueOf(gender.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid gender value: " + gender);
        }
    }
}
