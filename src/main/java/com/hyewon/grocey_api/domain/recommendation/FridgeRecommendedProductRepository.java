package com.hyewon.grocey_api.domain.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeRecommendedProductRepository extends JpaRepository<FridgeRecommendedProduct, Long> {
}
