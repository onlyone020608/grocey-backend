package com.hyewon.grocey_api.domain.recipe;

import com.hyewon.grocey_api.domain.user.User;
import jakarta.persistence.*;

@Entity
public class UserRecipe {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;


}
