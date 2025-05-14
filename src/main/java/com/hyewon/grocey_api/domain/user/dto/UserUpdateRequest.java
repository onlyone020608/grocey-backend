package com.hyewon.grocey_api.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String userName;
    private String email;

    @Builder
    public UserUpdateRequest(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public UserUpdateRequest() {
    }
}
