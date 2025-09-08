package com.hyewon.grocey_api.domain.auth.service;

import com.hyewon.grocey_api.domain.auth.dto.LoginRequest;
import com.hyewon.grocey_api.domain.auth.dto.SignupRequest;
import com.hyewon.grocey_api.domain.auth.dto.TokenRefreshRequest;
import com.hyewon.grocey_api.domain.auth.dto.TokenResponse;
import com.hyewon.grocey_api.domain.auth.security.JwtTokenProvider;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.entity.FridgeSnapshot;
import com.hyewon.grocey_api.domain.fridge.service.FridgeCommandService;
import com.hyewon.grocey_api.domain.fridge.service.FridgeIngredientManager;
import com.hyewon.grocey_api.domain.fridge.service.FridgeSnapshotCommandService;
import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.service.IngredientQueryService;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserCommandService;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final FridgeCommandService fridgeCommandService;
    private final IngredientQueryService ingredientQueryService;
    private final FridgeIngredientManager fridgeIngredientManager;
    private final FridgeSnapshotCommandService fridgeSnapshotCommandService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public User signup(SignupRequest request) {
        if (userQueryService.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Fridge fridge = Fridge.of(3.0, -18.0);
        fridgeCommandService.createFridge(fridge);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.of(request.getName(), request.getEmail(), encodedPassword);
        user.assignFridge(fridge); // 연관관계 설정

        for (long ingredientId : List.of(1L, 2L, 3L)) {
            Ingredient ingredient = ingredientQueryService.getIngredientById(ingredientId);

            FridgeIngredient fi = FridgeIngredient.of(
                    fridge,
                    ingredient,
                    false,
                    2,
                    LocalDate.now().plusDays(7)
            );
            fridgeIngredientManager.createFridgeIngredient(fi);
        }

        for (long ingredientId : List.of(7L, 8L)) {
            Ingredient ingredient = ingredientQueryService.getIngredientById(ingredientId);

            FridgeIngredient fi = FridgeIngredient.of(
                    fridge,
                    ingredient,
                    true,
                    2,
                    LocalDate.now().plusDays(30)
            );
            fridgeIngredientManager.createFridgeIngredient(fi);
        }

        List<FridgeIngredient> fridgeIngredients = fridgeIngredientManager.getByFridgeId(fridge.getId());

        for (FridgeIngredient fi : fridgeIngredients) {
            FridgeSnapshot snapshot = FridgeSnapshot.of(
                    fridge,
                    fi.getIngredient().getId(),
                    fi.getFreezer(),
                    fi.getQuantity()
            );
            fridgeSnapshotCommandService.createFridgeSnapshot(snapshot);
        }
        userCommandService.createUser(user);
        return user;
    }

    @Transactional
    public TokenResponse signupAndGenerateTokens(SignupRequest request) {
        User user = signup(request);

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        tokenService.storeRefreshToken(user.getId(), refreshToken, 7 * 24 * 60 * 60);
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userQueryService.getUserByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        tokenService.storeRefreshToken(user.getId(), refreshToken, 7 * 24 * 60 * 60);
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refresh(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        boolean valid = tokenService.validateRefreshToken(userId, refreshToken);
        if (!valid) {
            throw new IllegalArgumentException("Refresh token mismatch");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        return new TokenResponse(newAccessToken, refreshToken);
    }

    @Transactional
    public void logout(Long userId) {
        tokenService.deleteRefreshToken(userId);
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userQueryService.getUserById(userId);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedNewPassword);
    }
}
