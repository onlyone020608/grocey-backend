package com.hyewon.grocey_api.domain.recommendation.entity;

import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class FridgeRecommendedProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // <-- 상품 추천임
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_recommendation_id")
    private FridgeRecommendation fridgeRecommendation;

    private FridgeRecommendedProduct(Product product, FridgeRecommendation fridgeRecommendation) {
        this.product = product;
        this.fridgeRecommendation = fridgeRecommendation;
    }

    public static FridgeRecommendedProduct of(Product product, FridgeRecommendation fridgeRecommendation) {
        return new FridgeRecommendedProduct(product, fridgeRecommendation);
    }
}
