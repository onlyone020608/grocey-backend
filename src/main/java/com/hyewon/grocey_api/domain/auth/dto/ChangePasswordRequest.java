package com.hyewon.grocey_api.domain.auth.dto;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {}