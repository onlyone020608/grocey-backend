package com.hyewon.grocey_api.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    private UserFoodPreference(User user, FoodPreference foodPreference) {
        this.user = user;
        this.foodPreference = foodPreference;
    }

    public static UserFoodPreference of(User user, FoodPreference foodPreference) {
        return new UserFoodPreference(user, foodPreference);
    }
}
