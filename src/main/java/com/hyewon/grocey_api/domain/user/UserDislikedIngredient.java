package com.hyewon.grocey_api.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDislikedIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preference_ingredient_id")
    private PreferenceIngredient preferenceIngredient;

    public UserDislikedIngredient(User user, PreferenceIngredient preferenceIngredient) {
        this.user = user;
        this.preferenceIngredient = preferenceIngredient;
    }
}
