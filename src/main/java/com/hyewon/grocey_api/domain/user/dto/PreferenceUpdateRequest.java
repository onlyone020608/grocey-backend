package com.hyewon.grocey_api.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
public class PreferenceUpdateRequest {
    private List<Long> foodPreferenceIds;
    private List<Long> preferredIngredientIds;
    private List<Long> dislikedIngredientIds;

    public PreferenceUpdateRequest() {
    }

    @Builder
    public PreferenceUpdateRequest(List<Long> foodPreferenceIds, List<Long> preferredIngredientIds, List<Long> dislikedIngredientIds) {
        this.foodPreferenceIds = foodPreferenceIds;
        this.preferredIngredientIds = preferredIngredientIds;
        this.dislikedIngredientIds = dislikedIngredientIds;
    }
}
