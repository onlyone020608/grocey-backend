package com.hyewon.grocey_api.domain.recommendation.repository;

import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeRecommendedProductRepository extends JpaRepository<FridgeRecommendedProduct, Long> {
}
