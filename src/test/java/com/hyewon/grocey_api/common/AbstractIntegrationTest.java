package com.hyewon.grocey_api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyewon.grocey_api.domain.auth.service.AuthService;
import com.hyewon.grocey_api.domain.auth.dto.SignupRequest;
import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.repository.CartItemRepository;
import com.hyewon.grocey_api.domain.cart.service.CartService;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.repository.IngredientRepository;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.repository.ProductRepository;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeRepository;
import com.hyewon.grocey_api.domain.recipe.entity.SavedRecipe;
import com.hyewon.grocey_api.domain.recipe.repository.SavedRecipeRepository;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendation;
import com.hyewon.grocey_api.domain.recommendation.entity.FridgeRecommendedProduct;
import com.hyewon.grocey_api.domain.recommendation.entity.RecipeRecommendation;
import com.hyewon.grocey_api.domain.recommendation.entity.RecommendationType;
import com.hyewon.grocey_api.domain.recommendation.repository.FridgeRecommendationRepository;
import com.hyewon.grocey_api.domain.recommendation.repository.FridgeRecommendedProductRepository;
import com.hyewon.grocey_api.domain.recommendation.repository.RecipeRecommendationRepository;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.repository.UserAllergyRepository;
import com.hyewon.grocey_api.domain.user.repository.UserRepository;
import com.hyewon.grocey_api.domain.auth.security.JwtTokenProvider;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.util.UUID;


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



    protected User createTestUser(String name, String emailPrefix, String rawPassword) {
        String email = emailPrefix + "_" + UUID.randomUUID() + "@example.com";
        return authService.signup(new SignupRequest(email, rawPassword, name ));
    }

    protected String generateTokenFor(User user) {
        return jwtTokenProvider.generateAccessToken(user.getId());
    }

    protected FridgeIngredient setupFridgeIngredient(User user, Ingredient ingredient, boolean isFreezer,  int quantity) {
        Fridge fridge = user.getFridge();
        FridgeIngredient fi = FridgeIngredient.of(fridge, ingredient, isFreezer,  quantity, LocalDate.now().plusDays(7));
        return fridgeIngredientRepository.save(fi);
    }

    protected FridgeRecommendation setupFridgeRecommendation(User user) {
        FridgeRecommendation recommendation = FridgeRecommendation.of(user.getFridge());
        return fridgeRecommendationRepository.save(recommendation);
    }

    protected FridgeRecommendedProduct setupRecommendedProduct(Product product, FridgeRecommendation recommendation) {
        FridgeRecommendedProduct recProduct = FridgeRecommendedProduct.of(product, recommendation);
        return fridgeRecommendedProductRepository.save(recProduct);
    }

    protected RecipeRecommendation setupRecipeRecommendationByUser(User user, Recipe recipe) {
        RecipeRecommendation recommendation = RecipeRecommendation.ofUser(user, recipe, RecommendationType.PREFERENCE_BASED);
        return recipeRecommendationRepository.save(recommendation);
    }

    protected RecipeRecommendation setupRecipeRecommendationByFridge(User user, Recipe recipe) {
        RecipeRecommendation recommendation = RecipeRecommendation.ofFridge(user.getFridge(), recipe, RecommendationType.PREFERENCE_BASED);
        return recipeRecommendationRepository.save(recommendation);
    }

    protected void setupSavedRecipe(User user, Recipe recipe) {
        savedRecipeRepository.save(SavedRecipe.of(user, recipe));
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
