package com.hyewon.grocey_api.domain.user.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UserAllergyUpdateRequest {
    private List<Long> allergyIds;
}
