package com.hyewon.grocey_api.auth;

import com.hyewon.grocey_api.auth.dto.LoginRequest;
import com.hyewon.grocey_api.auth.dto.SignupRequest;
import com.hyewon.grocey_api.auth.dto.TokenRefreshRequest;
import com.hyewon.grocey_api.auth.dto.TokenResponse;
import com.hyewon.grocey_api.domain.cart.CartRepository;
import com.hyewon.grocey_api.domain.fridge.*;
import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.IngredientRepository;
import com.hyewon.grocey_api.domain.order.OrderRepository;
import com.hyewon.grocey_api.domain.recipe.SavedRecipeRepository;
import com.hyewon.grocey_api.domain.recommendation.RecipeRecommendationRepository;
import com.hyewon.grocey_api.domain.user.*;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import com.hyewon.grocey_api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final FridgeRepository fridgeRepository;
    private final IngredientRepository ingredientRepository;
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final FridgeSnapshotRepository fridgeSnapshotRepository;
    private final UserAllergyRepository userAllergyRepository;
    private final UserDislikedIngredientRepository  userDislikedIngredientRepository;
    private final UserPreferredIngredientRepository  userPreferredIngredientRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserFoodPreferenceRepository userFoodPreferenceRepository;
    private final SavedRecipeRepository savedRecipeRepository;
    private final RecipeRecommendationRepository recipeRecommendationRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final Map<Long, String> refreshTokenStore = new HashMap<>();



    public User signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }


        Fridge fridge = new Fridge(3.0, -18.0);
        fridgeRepository.save(fridge);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getName(), request.getEmail(), encodedPassword);
        user.assignFridge(fridge); // 연관관계 설정


        for (long ingredientId : List.of(1L, 2L, 3L)) {
            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new IllegalArgumentException("Ingredient not found: " + ingredientId));

            FridgeIngredient fi = new FridgeIngredient(
                    fridge,
                    ingredient,
                    false,
                    2, // 수량
                    LocalDate.now().plusDays(7)
            );
            fridgeIngredientRepository.save(fi);
        }

        List<FridgeIngredient> fridgeIngredients = fridgeIngredientRepository.findByFridgeId(fridge.getId());

        for (FridgeIngredient fi : fridgeIngredients) {
            FridgeSnapshot snapshot = new FridgeSnapshot(
                    fridge,
                    fi.getIngredient().getId(),
                    fi.getIsFreezer(),
                    fi.getQuantity()
            );
            fridgeSnapshotRepository.save(snapshot);
        }
        userRepository.save(user);
        return user;
    }

    public TokenResponse signupAndGenerateTokens(SignupRequest request) {
        User user = signup(request);

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        refreshTokenStore.put(user.getId(), refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        refreshTokenStore.put(user.getId(), refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String saved = refreshTokenStore.get(userId);

        if (!refreshToken.equals(saved)) {
            throw new IllegalArgumentException("Refresh token mismatch");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        return new TokenResponse(newAccessToken, refreshToken);
    }

    public void logout(Long userId) {
        refreshTokenStore.remove(userId);
    }

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));


        userAllergyRepository.deleteByUser(user);
        userDislikedIngredientRepository.deleteByUser(user);
        userFoodPreferenceRepository.deleteByUser(user);
        userPreferredIngredientRepository.deleteByUser(user);


        savedRecipeRepository.deleteByUser(user);
        recipeRecommendationRepository.deleteByUser(user);


        orderRepository.deleteByUser(user);
        cartRepository.deleteByUser(user);


        refreshTokenStore.remove(userId);
        userRepository.delete(user);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedNewPassword);
        userRepository.save(user);
    }


}
