package com.hyewon.grocey_api.domain.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<FridgeRecommendation, Long> {
}
