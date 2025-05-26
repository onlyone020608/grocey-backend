package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.User;
import lombok.Getter;

@Getter
public class UserSummaryDto {
    private final String name;

    public UserSummaryDto(User user) {
        this.name = user.getUserName();
    }
}
