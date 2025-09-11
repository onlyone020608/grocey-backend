package com.hyewon.grocey_api.domain.auth.dto;

import lombok.Builder;

@Builder
public record TokenRefreshRequest(
        String refreshToken
) {}
