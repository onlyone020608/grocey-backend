package com.hyewon.grocey_api.domain.user;

import jakarta.persistence.*;

@Entity
public class UserFoodPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_preference_id")
    private FoodPreference foodPreference;
}
