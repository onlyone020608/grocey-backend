package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSummaryResponse {
    private final String name;

    public UserSummaryResponse(User user) {
        this.name = user.getUserName();
    }
}
