package com.hyewon.grocey_api.domain.product;

import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String brandName;
    private String productName;
    private double price;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public Product(String productName, String brandName, double price, String imageUrl) {
        this.imageUrl = imageUrl;
        this.price = price;
        this.productName = productName;
        this.brandName = brandName;
    }

    public void assignIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
