package com.hyewon.grocey_api.domain.user.dto;

import com.hyewon.grocey_api.domain.user.User;
import lombok.Getter;

@Getter
public class UserDetailDto {
    private String userName;
    private String email;

    public UserDetailDto(User user) {
        this.userName = user.getUserName();
        this.email = user.getEmail();
    }
}
