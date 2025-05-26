package com.hyewon.grocey_api.global.exception;

public class RecommendationNotFoundException extends RuntimeException {
    public RecommendationNotFoundException(String target, String basedOn, Long id) {
        super("No " + target + " recommendation based on " + basedOn + " found for id = " + id);
    }

    public static RecommendationNotFoundException forFridgeProduct(Long fridgeId) {
        return new RecommendationNotFoundException("product", "fridge", fridgeId);
    }

    public static RecommendationNotFoundException forUserRecipe(Long userId) {
        return new RecommendationNotFoundException("recipe", "user", userId);
    }

    public static RecommendationNotFoundException forFridgeRecipe(Long fridgeId) {
        return new RecommendationNotFoundException("recipe", "fridge", fridgeId);
    }

}
