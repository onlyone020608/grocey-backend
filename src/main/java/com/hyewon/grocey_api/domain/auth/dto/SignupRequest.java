package com.hyewon.grocey_api.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignupRequest(
        @JsonProperty("email") String email,
        @JsonProperty("password") String password,
        @JsonProperty("name") String name
) {}