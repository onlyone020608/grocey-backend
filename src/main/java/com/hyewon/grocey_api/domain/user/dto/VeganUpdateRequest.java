package com.hyewon.grocey_api.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class VeganUpdateRequest {
    @JsonProperty("vegan")
    private boolean isVegan;

    public VeganUpdateRequest() {}

    public VeganUpdateRequest(boolean isVegan) {
        this.isVegan = isVegan;
    }


}
