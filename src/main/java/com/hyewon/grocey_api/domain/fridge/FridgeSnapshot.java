package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private Boolean isFreezer;

    private int quantity;

    public FridgeSnapshot(Fridge fridge, Long ingredient_id, Boolean isFreezer, int quantity) {
        this.fridge = fridge;
        this.ingredient_id = ingredient_id;
        this.isFreezer = isFreezer;
        this.quantity = quantity;
    }






}
