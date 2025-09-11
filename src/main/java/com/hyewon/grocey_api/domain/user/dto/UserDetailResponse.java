package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.entity.User;

public record UserDetailResponse(
        String userName,
        String email
) {
    public static UserDetailResponse from(User user) {
        return new UserDetailResponse(
                user.getUsername(),
                user.getEmail()
        );
    }
}
