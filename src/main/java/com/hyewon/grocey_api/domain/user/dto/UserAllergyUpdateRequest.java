package com.hyewon.grocey_api.domain.user.dto;

import java.util.List;

public record UserAllergyUpdateRequest(
        List<Long> allergyIds
) {}
