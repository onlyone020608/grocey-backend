package com.hyewon.grocey_api.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(
        @JsonProperty("email") String email,
        @JsonProperty("password") String password
) {}
