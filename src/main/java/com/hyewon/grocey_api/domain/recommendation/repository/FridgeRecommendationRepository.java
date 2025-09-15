package com.hyewon.grocey_api.domain.recommendation.repository;

import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeRecommendationRepository extends JpaRepository<FridgeRecommendation, Long> {
}
