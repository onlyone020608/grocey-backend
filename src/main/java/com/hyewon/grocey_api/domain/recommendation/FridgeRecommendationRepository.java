package com.hyewon.grocey_api.domain.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FridgeRecommendationRepository extends JpaRepository<FridgeRecommendation, Long> {
    Optional<FridgeRecommendation> findTopByFridgeIdOrderByCreatedAtDesc(Long fridgeId);
}
