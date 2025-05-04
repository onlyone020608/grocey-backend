package com.hyewon.grocey_api.domain.product;

import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String brandName;
    private String productName;
    private int price;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;


}
