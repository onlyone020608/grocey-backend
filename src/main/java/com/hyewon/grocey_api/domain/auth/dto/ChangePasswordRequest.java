package com.hyewon.grocey_api.domain.auth.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
