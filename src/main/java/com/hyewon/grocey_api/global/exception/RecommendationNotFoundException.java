package com.hyewon.grocey_api.global.exception;

public class RecommendationNotFoundException extends RuntimeException {
    public RecommendationNotFoundException(Long fridgeId) {
        super("No fridge recommendation found for fridgeId = " + fridgeId);
    }

}
