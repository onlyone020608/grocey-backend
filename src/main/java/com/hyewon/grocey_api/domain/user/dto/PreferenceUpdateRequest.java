package com.hyewon.grocey_api.domain.user.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PreferenceUpdateRequest(
        List<Long> foodPreferenceIds,
        List<Long> preferredIngredientIds,
        List<Long> dislikedIngredientIds
) {}
