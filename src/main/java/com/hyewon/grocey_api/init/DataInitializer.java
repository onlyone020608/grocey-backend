package com.hyewon.grocey_api.init;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.IngredientRepository;
import com.hyewon.grocey_api.domain.user.AgeGroup;
import com.hyewon.grocey_api.domain.user.Gender;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final IngredientRepository ingredientRepository;
    private final FridgeRepository fridgeRepository;
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void initDummyData() {
        Ingredient i = ingredientRepository.save(new Ingredient("계란", "egg.png"));
        Fridge f = fridgeRepository.save(new Fridge(4.0, -18.0));

        // userId=1 유저가 있다고 가정하고, 해당 유저에 fridge 연결
        User u = userRepository.save(new User("TestUser", "onlyone@naver.com", "1234!", AgeGroup.TWENTIES, Gender.FEMALE));
        u.assignFridge(f);

        fridgeIngredientRepository.save(
                new FridgeIngredient(f, i, false, 10, LocalDate.of(2025, 6, 1))
        );
    }
}
