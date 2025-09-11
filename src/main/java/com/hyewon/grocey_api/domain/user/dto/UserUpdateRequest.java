package com.hyewon.grocey_api.domain.user.dto;

import lombok.Builder;

@Builder
public record UserUpdateRequest(
        String userName,
        String email
) {}
