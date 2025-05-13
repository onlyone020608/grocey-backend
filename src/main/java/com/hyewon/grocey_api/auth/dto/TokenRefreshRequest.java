package com.hyewon.grocey_api.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenRefreshRequest {
    private final String refreshToken;

}
