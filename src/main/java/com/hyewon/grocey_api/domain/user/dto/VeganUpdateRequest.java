package com.hyewon.grocey_api.domain.user.dto;

import lombok.Getter;

@Getter
public class VeganUpdateRequest {
    private boolean isVegan;

    public VeganUpdateRequest() {}

    public VeganUpdateRequest(boolean isVegan) {
        this.isVegan = isVegan;
    }


}
