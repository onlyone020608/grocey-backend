package com.hyewon.grocey_api.init;

import com.hyewon.grocey_api.domain.cart.Cart;
import com.hyewon.grocey_api.domain.cart.CartItem;
import com.hyewon.grocey_api.domain.cart.CartItemRepository;
import com.hyewon.grocey_api.domain.cart.CartRepository;
import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.IngredientRepository;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.product.ProductRepository;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import com.hyewon.grocey_api.domain.recipe.RecipeIngredient;
import com.hyewon.grocey_api.domain.recipe.RecipeIngredientRepository;
import com.hyewon.grocey_api.domain.recipe.RecipeRepository;
import com.hyewon.grocey_api.domain.recommendation.FridgeRecommendation;
import com.hyewon.grocey_api.domain.recommendation.FridgeRecommendationRepository;
import com.hyewon.grocey_api.domain.recommendation.FridgeRecommendedProduct;
import com.hyewon.grocey_api.domain.recommendation.FridgeRecommendedProductRepository;
import com.hyewon.grocey_api.domain.user.AgeGroup;
import com.hyewon.grocey_api.domain.user.Gender;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final IngredientRepository ingredientRepository;
    private final FridgeRepository fridgeRepository;
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final FridgeRecommendationRepository fridgeRecommendationRepository;
    private final FridgeRecommendedProductRepository fridgeRecommendedProductRepository;



    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Ingredient egg = ingredientRepository.save(new Ingredient("계란", "egg.png"));
        Ingredient milk = ingredientRepository.save(new Ingredient("우유", "milk.png"));
        Fridge f = fridgeRepository.save(new Fridge(4.0, -18.0));
        // userId=1 유저가 있다고 가정하고, 해당 유저에 fridge 연결
        User u = new User("TestUser22222", "onlyone@naver.com", "1234!", AgeGroup.TWENTIES, Gender.FEMALE);
        System.out.println("Fridge User: " + f.getId());
        u.assignFridge(f);
        userRepository.save(u);

        fridgeIngredientRepository.save(
                new FridgeIngredient(f, egg, false, 10, LocalDate.of(2025, 6, 1))
        );

        Recipe recipe = recipeRepository.save(new Recipe(
                "프렌치 토스트",
                "1. 계란을 풀어줍니다.\n2. 우유를 섞습니다.\n3. 팬에 부어 익힙니다.",
                10,
                2
        ));

        recipeIngredientRepository.save(new RecipeIngredient(recipe, egg, "1개"));
        recipeIngredientRepository.save(new RecipeIngredient(recipe, milk, "100ml"));

        // 장바구니 테스트
        Product product1 = productRepository.save(new Product("계란 10개입", "롯데", 2980, "egg-pack.png"));
        Product product2 = productRepository.save(new Product("우유 1L", "롯데", 2450, "milk-1l.png"));
        Cart cart = new Cart(u, f);
        cartRepository.save(cart);
        CartItem item1 = new CartItem(product1, 2);
        CartItem item2 = new CartItem(product2, 1);

        cart.addCartItem(item1);
        cart.addCartItem(item2);

        cartItemRepository.save(item1);
        cartItemRepository.save(item2);

        // 추천 장바구니 테스트용 데이터
        FridgeRecommendation recommendation = new FridgeRecommendation(f);
        fridgeRecommendationRepository.save(recommendation);

        FridgeRecommendedProduct rp1 = new FridgeRecommendedProduct(product1, recommendation);
        FridgeRecommendedProduct rp2 = new FridgeRecommendedProduct(product2, recommendation);

        fridgeRecommendedProductRepository.save(rp1);
        fridgeRecommendedProductRepository.save(rp2);


    }
}
