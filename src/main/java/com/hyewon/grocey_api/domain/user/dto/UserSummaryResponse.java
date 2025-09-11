package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.entity.User;

public record UserSummaryResponse(
        String name
) {
    public static UserSummaryResponse from(User user) {
        return new UserSummaryResponse(user.getUsername());
    }
}
