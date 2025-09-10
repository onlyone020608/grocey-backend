package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSummaryResponse {
    private final String name;

    private UserSummaryResponse(String name) {
        this.name = name;
    }

    public static UserSummaryResponse from(User user) {
        return new UserSummaryResponse(user.getUsername());
    }
}
