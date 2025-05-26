package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FridgeRecommendation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;


    @OneToMany(mappedBy = "fridgeRecommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FridgeRecommendedProduct> recommendedProducts = new ArrayList<>();

    public FridgeRecommendation(Fridge fridge) {
        this.fridge = fridge;
    }
}
