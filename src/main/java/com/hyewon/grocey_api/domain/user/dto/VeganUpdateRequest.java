package com.hyewon.grocey_api.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VeganUpdateRequest(
        @JsonProperty("vegan")
        boolean isVegan
) {}