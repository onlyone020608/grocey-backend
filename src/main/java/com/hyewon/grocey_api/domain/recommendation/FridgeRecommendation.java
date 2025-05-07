package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FridgeRecommendation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;


    @OneToMany(mappedBy = "fridgeRecommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FridgeRecommendedProduct> recommendedIngredients = new ArrayList<>();


}
