package com.hyewon.grocey_api.init;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.IngredientRepository;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import com.hyewon.grocey_api.domain.recipe.RecipeIngredient;
import com.hyewon.grocey_api.domain.recipe.RecipeIngredientRepository;
import com.hyewon.grocey_api.domain.recipe.RecipeRepository;
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
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @PostConstruct
    public void initDummyData() {
        Ingredient i = ingredientRepository.save(new Ingredient("계란", "egg.png"));
        Ingredient egg = ingredientRepository.save(new Ingredient("계란", "egg.png"));
        Ingredient milk = ingredientRepository.save(new Ingredient("우유", "milk.png"));
        Fridge f = fridgeRepository.save(new Fridge(4.0, -18.0));

        // userId=1 유저가 있다고 가정하고, 해당 유저에 fridge 연결
        User u = userRepository.save(new User("TestUser", "onlyone@naver.com", "1234!", AgeGroup.TWENTIES, Gender.FEMALE));
        u.assignFridge(f);

        fridgeIngredientRepository.save(
                new FridgeIngredient(f, i, false, 10, LocalDate.of(2025, 6, 1))
        );

        Recipe recipe = recipeRepository.save(new Recipe(
                "프렌치 토스트",
                "1. 계란을 풀어줍니다.\n2. 우유를 섞습니다.\n3. 팬에 부어 익힙니다.",
                10,
                2
        ));

        recipeIngredientRepository.save(new RecipeIngredient(recipe, egg, "1개"));
        recipeIngredientRepository.save(new RecipeIngredient(recipe, milk, "100ml"));
    }
}
