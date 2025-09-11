package com.hyewon.grocey_api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyewon.grocey_api.domain.auth.dto.SignupRequest;
import com.hyewon.grocey_api.domain.auth.security.JwtTokenProvider;
import com.hyewon.grocey_api.domain.auth.service.AuthService;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.repository.IngredientRepository;
import com.hyewon.grocey_api.domain.product.repository.ProductRepository;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.entity.SavedRecipe;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeRepository;
import com.hyewon.grocey_api.domain.recipe.repository.SavedRecipeRepository;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.repository.UserAllergyRepository;
import com.hyewon.grocey_api.domain.user.repository.UserRepository;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected JwtTokenProvider jwtTokenProvider;
    @Autowired protected PasswordEncoder passwordEncoder;
    @Autowired protected UserRepository userRepository;
    @Autowired protected UserAllergyRepository userAllergyRepository;
    @Autowired protected IngredientRepository ingredientRepository;
    @Autowired protected ProductRepository productRepository;
    @Autowired private AuthService authService;
    @Autowired private FridgeIngredientRepository fridgeIngredientRepository;
    @Autowired protected RecipeRepository recipeRepository;
    @Autowired private SavedRecipeRepository savedRecipeRepository;

    protected User createTestUser() {
        String email = "user_" + UUID.randomUUID() + "@example.com";
        return authService.signup(new SignupRequest(email, "password", "TestUser"));
    }

    protected String generateTokenFor(User user) {
        return jwtTokenProvider.generateAccessToken(user.getId());
    }

    protected FridgeIngredient setupFridgeIngredient(User user, Ingredient ingredient, boolean isFreezer,  int quantity) {
        Fridge fridge = user.getFridge();
        FridgeIngredient fi = FridgeIngredient.of(fridge, ingredient, isFreezer,  quantity, LocalDate.now().plusDays(7));
        return fridgeIngredientRepository.save(fi);
    }

    protected void setupSavedRecipe(User user, Recipe recipe) {
        savedRecipeRepository.save(SavedRecipe.of(user, recipe));
    }
}
