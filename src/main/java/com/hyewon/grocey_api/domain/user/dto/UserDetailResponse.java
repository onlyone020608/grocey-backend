package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserDetailResponse {
    private String userName;
    private String email;

    private UserDetailResponse(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public static UserDetailResponse from(User user) {
        return new UserDetailResponse(
                user.getUsername(),
                user.getEmail()
        );
    }
}
