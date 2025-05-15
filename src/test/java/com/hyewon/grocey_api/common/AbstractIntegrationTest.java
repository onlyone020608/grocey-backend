package com.hyewon.grocey_api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyewon.grocey_api.auth.AuthService;
import com.hyewon.grocey_api.auth.dto.SignupRequest;
import com.hyewon.grocey_api.domain.cart.*;
import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.IngredientRepository;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.product.ProductRepository;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import com.hyewon.grocey_api.domain.recipe.RecipeRepository;
import com.hyewon.grocey_api.domain.recipe.SavedRecipe;
import com.hyewon.grocey_api.domain.recipe.SavedRecipeRepository;
import com.hyewon.grocey_api.domain.recommendation.*;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserAllergyRepository;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.security.JwtTokenProvider;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected UserRepository userRepository;

    @Autowired protected UserAllergyRepository userAllergyRepository;

    @Autowired protected IngredientRepository ingredientRepository;

    @Autowired protected ProductRepository productRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private FridgeIngredientRepository fridgeIngredientRepository;

    @Autowired
    private FridgeRecommendationRepository fridgeRecommendationRepository;

    @Autowired
    private FridgeRecommendedProductRepository fridgeRecommendedProductRepository;

    @Autowired protected RecipeRepository recipeRepository;
    @Autowired protected RecipeRecommendationRepository recipeRecommendationRepository;

    @Autowired
    private SavedRecipeRepository savedRecipeRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemRepository cartItemRepository;



    protected User createTestUser(String name, String email, String rawPassword) {
        return authService.signup(new SignupRequest(name, email, passwordEncoder.encode(rawPassword)));
    }

    protected String generateTokenFor(User user) {
        return jwtTokenProvider.generateAccessToken(user.getId());
    }

    protected FridgeIngredient setupFridgeIngredient(User user, Ingredient ingredient, boolean isFreezer,  int quantity) {
        Fridge fridge = user.getFridge();
        FridgeIngredient fi = new FridgeIngredient(fridge, ingredient, isFreezer,  quantity, LocalDate.now().plusDays(7));
        return fridgeIngredientRepository.save(fi);
    }

    protected FridgeRecommendation setupFridgeRecommendation(User user) {
        FridgeRecommendation recommendation = new FridgeRecommendation(user.getFridge());
        return fridgeRecommendationRepository.save(recommendation);
    }

    protected FridgeRecommendedProduct setupRecommendedProduct(Product product, FridgeRecommendation recommendation) {
        FridgeRecommendedProduct recProduct = new FridgeRecommendedProduct(product, recommendation);
        return fridgeRecommendedProductRepository.save(recProduct);
    }

    protected RecipeRecommendation setupRecipeRecommendationByUser(User user, Recipe recipe) {
        RecipeRecommendation recommendation = new RecipeRecommendation(user, recipe, RecommendationType.PREFERENCE_BASED);
        return recipeRecommendationRepository.save(recommendation);
    }

    protected RecipeRecommendation setupRecipeRecommendationByFridge(User user, Recipe recipe) {
        RecipeRecommendation recommendation = new RecipeRecommendation(user.getFridge(), recipe, RecommendationType.PREFERENCE_BASED);
        return recipeRecommendationRepository.save(recommendation);
    }

    protected void setupSavedRecipe(User user, Recipe recipe) {
        savedRecipeRepository.save(new SavedRecipe(user, recipe));
    }

    protected CartItem addCartItemFor(User user, Product product, int quantity) {
        AddCartItemRequest request = new AddCartItemRequest(product.getId(), quantity);
        cartService.addCartItem(user.getId(), request);  // 여기를 서비스 단 호출로
        return cartItemRepository.findAll().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("CartItem not found"));
    }

}
