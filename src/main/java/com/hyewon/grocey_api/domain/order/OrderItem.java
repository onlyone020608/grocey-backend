package com.hyewon.grocey_api.domain.order;

import com.hyewon.grocey_api.domain.ingredients.Ingredient;
import com.hyewon.grocey_api.domain.product.Product;
import jakarta.persistence.*;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String address;

    private int totalPrice;

}
