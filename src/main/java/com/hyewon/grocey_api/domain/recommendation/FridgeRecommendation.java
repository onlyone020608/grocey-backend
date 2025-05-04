package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FridgeRecommendation {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FridgeRecommendedIngredient> recommendedIngredients = new ArrayList<>();


}
