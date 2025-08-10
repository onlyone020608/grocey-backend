package com.hyewon.grocey_api.domain.recommendation.entity;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class FridgeRecommendation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    @OneToMany(mappedBy = "fridgeRecommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FridgeRecommendedProduct> recommendedProducts = new ArrayList<>();

    private FridgeRecommendation(Fridge fridge) {
        this.fridge = fridge;
    }

    public static FridgeRecommendation of(Fridge fridge) {
        return new FridgeRecommendation(fridge);
    }

    public void addRecommendationProduct(FridgeRecommendedProduct product) {
        recommendedProducts.add(product);
    }
}
