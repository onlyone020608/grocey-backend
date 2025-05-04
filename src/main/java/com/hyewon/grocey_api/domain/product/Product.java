package com.hyewon.grocey_api.domain.product;

import com.hyewon.grocey_api.domain.ingredients.Ingredient;
import jakarta.persistence.*;

@Entity
public class Product {
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
