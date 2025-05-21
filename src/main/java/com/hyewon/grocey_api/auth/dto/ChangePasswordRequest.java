package com.hyewon.grocey_api.auth.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
