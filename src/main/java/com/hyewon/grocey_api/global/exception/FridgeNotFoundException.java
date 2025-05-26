package com.hyewon.grocey_api.global.exception;

import lombok.Getter;

@Getter
public class FridgeNotFoundException extends RuntimeException {
  private final ErrorCode errorCode = ErrorCode.FRIDGE_NOT_FOUND;

  public FridgeNotFoundException(Long fridgeId) {
    super("Fridge not found (id=" + fridgeId + ")");
  }

}
