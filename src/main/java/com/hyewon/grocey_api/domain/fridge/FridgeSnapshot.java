package com.hyewon.grocey_api.domain.fridge;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class FridgeSnapshot {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    private String ingredient_id;

    private Boolean isFreezer;

    private int quantity;
    @CreationTimestamp
    private LocalDateTime createdAt;





}
