package com.hyewon.grocey_api.domain.user.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PreferenceUpdateRequest {
    private List<Long> foodPreferenceIds;
    private List<Long> preferredIngredientIds;
    private List<Long> dislikedIngredientIds;
}
