package com.hyewon.grocey_api.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SignupRequest {
    private final String email;
    private final String password;
    private final String name;

    @JsonCreator
    public SignupRequest(@JsonProperty("email") String email,
                         @JsonProperty("password") String password,
                         @JsonProperty("name") String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

}
