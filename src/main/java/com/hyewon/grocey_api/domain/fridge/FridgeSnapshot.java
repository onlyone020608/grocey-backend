package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class FridgeSnapshot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    private String ingredient_id;

    private Boolean isFreezer;

    private int quantity;






}
