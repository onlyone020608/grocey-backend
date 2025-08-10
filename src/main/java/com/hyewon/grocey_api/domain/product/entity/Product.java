package com.hyewon.grocey_api.domain.product.entity;

import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private double price;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private Product(String name, String brand, double price, String imageUrl) {
        this.imageUrl = imageUrl;
        this.price = price;
        this.name = name;
        this.brand = brand;
    }

    public static Product of(String name, String brand, double price, String imageUrl) {
        return new Product(name, brand, price, imageUrl);
    }

    public void assignIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
