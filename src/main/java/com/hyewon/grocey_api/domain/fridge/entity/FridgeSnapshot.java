package com.hyewon.grocey_api.domain.fridge.entity;

import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FridgeSnapshot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    private Long ingredient_id;

    private Boolean freezer;

    private int quantity;

    private FridgeSnapshot(Fridge fridge, Long ingredient_id, Boolean freezer, int quantity) {
        this.fridge = fridge;
        this.ingredient_id = ingredient_id;
        this.freezer = freezer;
        this.quantity = quantity;
    }

    public static FridgeSnapshot of (Fridge fridge, Long ingredient_id, Boolean freezer, int quantity) {
        return new FridgeSnapshot(fridge, ingredient_id, freezer, quantity);
    }
}
