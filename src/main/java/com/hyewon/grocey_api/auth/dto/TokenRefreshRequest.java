package com.hyewon.grocey_api.auth.dto;

import lombok.Getter;

@Getter
public class TokenRefreshRequest {
    private final String refreshToken;

    private TokenRefreshRequest(Builder builder) {
        this.refreshToken = builder.refreshToken;
    }

    public static class Builder {
        private String refreshToken;

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public TokenRefreshRequest build() {
            return new TokenRefreshRequest(this);
        }
    }

}
