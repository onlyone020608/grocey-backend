package com.hyewon.grocey_api.domain.user.dto;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String userName;
    private String email;
}
