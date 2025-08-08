package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserDetailResponse {
    private String userName;
    private String email;

    public UserDetailResponse(User user) {
        this.userName = user.getUsername();
        this.email = user.getEmail();
    }
}
