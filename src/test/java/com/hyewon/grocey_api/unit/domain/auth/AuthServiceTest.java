package com.hyewon.grocey_api.unit.domain.auth;

import com.hyewon.grocey_api.domain.auth.service.AuthService;
import com.hyewon.grocey_api.domain.auth.dto.LoginRequest;
import com.hyewon.grocey_api.domain.auth.dto.SignupRequest;
import com.hyewon.grocey_api.domain.auth.dto.TokenRefreshRequest;
import com.hyewon.grocey_api.domain.auth.dto.TokenResponse;
import com.hyewon.grocey_api.domain.cart.repository.CartRepository;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeRepository;
import com.hyewon.grocey_api.domain.fridge.service.FridgeCommandService;
import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.repository.IngredientRepository;
import com.hyewon.grocey_api.domain.order.repository.OrderRepository;
import com.hyewon.grocey_api.domain.recipe.repository.SavedRecipeRepository;
import com.hyewon.grocey_api.domain.recommendation.repository.RecipeRecommendationRepository;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.repository.*;
import com.hyewon.grocey_api.domain.user.service.UserCommandService;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.domain.auth.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserQueryService userQueryService;
    @Mock private UserCommandService userCommandService;
    @Mock private FridgeCommandService fridgeCommandService;
    @Mock private UserAllergyRepository userAllergyRepository;
    @Mock private UserDislikedIngredientRepository userDislikedIngredientRepository;
    @Mock private UserFoodPreferenceRepository userFoodPreferenceRepository;
    @Mock private UserPreferredIngredientRepository userPreferredIngredientRepository;
    @Mock private SavedRecipeRepository savedRecipeRepository;
    @Mock private RecipeRecommendationRepository recipeRecommendationRepository;
    @Mock private IngredientRepository ingredientRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private FridgeIngredientRepository fridgeIngredientRepository;
    @Mock private CartRepository cartRepository;

    @Mock private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private Ingredient ingredient3;
    private Ingredient ingredient7;
    private Ingredient ingredient8;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest("tester", "tester@email.com", "securepass");
        ingredient1 = Ingredient.builder()
                .id(1L)
                .build();
        ingredient2 = Ingredient.builder()
                .id(2L)
                .build();
        ingredient3 = Ingredient.builder()
                .id(3L)
                .build();
        ingredient7 = Ingredient.builder()
                .id(7L)
                .build();
        ingredient8 = Ingredient.builder()
                .id(8L)
                .build();

    }

    @Test
    @DisplayName("signup - signup - successful registration")
    void signup_shouldSucceed() {
        // given
        given(userQueryService.existsByEmail(signupRequest.getEmail())).willReturn(false);
        given(ingredientRepository.findById(1L)).willReturn(Optional.of(ingredient1));
        given(ingredientRepository.findById(2L)).willReturn(Optional.of(ingredient2));
        given(ingredientRepository.findById(3L)).willReturn(Optional.of(ingredient3));
        given(ingredientRepository.findById(7L)).willReturn(Optional.of(ingredient7));
        given(ingredientRepository.findById(8L)).willReturn(Optional.of(ingredient8));

        // when
        authService.signup(signupRequest);

        // then
        verify(userQueryService).existsByEmail(signupRequest.getEmail());
        verify(fridgeCommandService).createFridge(any(Fridge.class));
    }

    @Test
    @DisplayName("signup - throws exception when email is already in use")
    void signup_shouldThrowWhenEmailAlreadyExists() {
        // given
        given(userQueryService.existsByEmail(signupRequest.getEmail())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.signup(signupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");

        verify(fridgeCommandService, never()).createFridge(any());
        verify(userCommandService, never()).createUser(any());
    }

    @Test
    @DisplayName("login - returns access and refresh token when credentials are valid")
    void login_shouldReturnTokens_whenCredentialsAreValid() {
        // given
        LoginRequest request = new LoginRequest("user@email.com", "password");
        User user = new User("tester", "user@email.com", "encoded-password");
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userQueryService.getUserByEmail("user@email.com")).willReturn(user);
        given(passwordEncoder.matches("password", "encoded-password")).willReturn(true);
        given(jwtTokenProvider.generateAccessToken(1L)).willReturn("access-token");
        given(jwtTokenProvider.generateRefreshToken(1L)).willReturn("refresh-token");

        // when
        TokenResponse response = authService.login(request);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("refresh - returns new access token when refresh token is valid")
    void refresh_shouldReturnNewAccessToken_whenValid() {
        // given
        Long userId = 1L;
        String oldRefreshToken = "valid-refresh-token";
        TokenRefreshRequest request = TokenRefreshRequest.builder()
                .refreshToken(oldRefreshToken)
                .build();

        given(jwtTokenProvider.validateToken(oldRefreshToken)).willReturn(true);
        given(jwtTokenProvider.getUserIdFromToken(oldRefreshToken)).willReturn(userId);
        given(jwtTokenProvider.generateAccessToken(userId)).willReturn("new-access-token");

        // refreshTokenStore 세팅
        ReflectionTestUtils.setField(authService, "refreshTokenStore", Map.of(userId, oldRefreshToken));

        // when
        TokenResponse response = authService.refresh(request);

        // then
        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo(oldRefreshToken);
    }

    @Test
    @DisplayName("refresh - throws exception when token is invalid")
    void refresh_shouldThrowException_whenTokenIsInvalid() {
        // given
        String invalidToken = "invalid-refresh-token";
        TokenRefreshRequest request = TokenRefreshRequest.builder()
                .refreshToken(invalidToken)
                .build();

        given(jwtTokenProvider.validateToken(invalidToken)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid refresh token");
    }

    @Test
    @DisplayName("refresh - throws exception when token does not match stored token")
    void refresh_shouldThrowException_whenTokenMismatch() {
        // given
        Long userId = 1L;
        String oldToken = "valid-refresh-token";
        String storedToken = "different-token";

        TokenRefreshRequest request = TokenRefreshRequest.builder()
                .refreshToken(oldToken)
                .build();

        given(jwtTokenProvider.validateToken(oldToken)).willReturn(true);
        given(jwtTokenProvider.getUserIdFromToken(oldToken)).willReturn(userId);

        ReflectionTestUtils.setField(authService, "refreshTokenStore", Map.of(userId, storedToken));

        // when & then
        assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Refresh token mismatch");
    }

    @Test
    @DisplayName("logout - removes refresh token from store")
    void logout_shouldRemoveRefreshToken() {
        // given
        Long userId = 1L;
        String refreshToken = "refresh-token";
        ReflectionTestUtils.setField(authService, "refreshTokenStore", new HashMap<>(Map.of(userId, refreshToken)));

        // when
        authService.logout(userId);

        // then
        Map<Long, String> store = (Map<Long, String>) ReflectionTestUtils.getField(authService, "refreshTokenStore");
        assertThat(store.containsKey(userId)).isFalse();
    }

    @Test
    @DisplayName("withdraw - deletes user and all related entities when user exists")
    void withdraw_shouldDeleteUserAndAllRelations_whenUserExists() {
        // given
        Long userId = 1L;
        User user = new User("tester", "tester@email.com", "encodedPass");
        ReflectionTestUtils.setField(user, "id", userId);

        given(userQueryService.getUserById(userId)).willReturn(user);

        Map<Long, String> mockStore = new HashMap<>(Map.of(userId, "token"));
        ReflectionTestUtils.setField(authService, "refreshTokenStore", mockStore);

        // when
        authService.withdraw(userId);

        // then
        verify(userAllergyRepository).deleteByUser(user);
        verify(userDislikedIngredientRepository).deleteByUser(user);
        verify(userFoodPreferenceRepository).deleteByUser(user);
        verify(userPreferredIngredientRepository).deleteByUser(user);

        verify(savedRecipeRepository).deleteByUser(user);
        verify(recipeRecommendationRepository).deleteByUser(user);

        verify(orderRepository).deleteByUser(user);
        verify(cartRepository).deleteByUser(user);

        Map<Long, String> store = (Map<Long, String>) ReflectionTestUtils.getField(authService, "refreshTokenStore");
        assertThat(store.containsKey(userId)).isFalse();
    }
}