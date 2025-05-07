package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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

}
