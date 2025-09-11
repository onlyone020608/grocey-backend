package com.hyewon.grocey_api.domain.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}