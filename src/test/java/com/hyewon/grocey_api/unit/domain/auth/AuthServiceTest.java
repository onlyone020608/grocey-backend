package com.hyewon.grocey_api.unit.domain.auth;

import com.hyewon.grocey_api.domain.auth.dto.LoginRequest;
import com.hyewon.grocey_api.domain.auth.dto.SignupRequest;
import com.hyewon.grocey_api.domain.auth.dto.TokenRefreshRequest;
import com.hyewon.grocey_api.domain.auth.dto.TokenResponse;
import com.hyewon.grocey_api.domain.auth.security.JwtTokenProvider;
import com.hyewon.grocey_api.domain.auth.service.AuthService;
import com.hyewon.grocey_api.domain.auth.service.TokenService;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.service.FridgeCommandService;
import com.hyewon.grocey_api.domain.fridge.service.FridgeIngredientManager;
import com.hyewon.grocey_api.domain.fridge.service.FridgeSnapshotCommandService;
import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.service.IngredientQueryService;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserCommandService;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.domain.user.service.UserWithdrawalService;
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
    @Mock private UserWithdrawalService userWithdrawalService;
    @Mock private IngredientQueryService ingredientQueryService;
    @Mock private FridgeIngredientManager fridgeIngredientManager;
    @Mock private FridgeSnapshotCommandService fridgeSnapshotCommandService;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private TokenService tokenService;
    @InjectMocks private AuthService authService;

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
    @DisplayName("signs up user when request is valid")
    void shouldSignUpUser_whenRequestValid() {
        // given
        given(userQueryService.existsByEmail(signupRequest.getEmail())).willReturn(false);
        given(ingredientQueryService.getIngredientById(1L)).willReturn(ingredient1);
        given(ingredientQueryService.getIngredientById(2L)).willReturn(ingredient2);
        given(ingredientQueryService.getIngredientById(3L)).willReturn(ingredient3);
        given(ingredientQueryService.getIngredientById(7L)).willReturn(ingredient7);
        given(ingredientQueryService.getIngredientById(8L)).willReturn(ingredient8);

        // when
        authService.signup(signupRequest);

        // then
        verify(userQueryService).existsByEmail(signupRequest.getEmail());
        verify(fridgeCommandService).createFridge(any(Fridge.class));
    }

    @Test
    @DisplayName("throws exception when email is already in use during signup")
    void shouldThrowException_whenEmailAlreadyInUse() {
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
    @DisplayName("returns access and refresh tokens when login credentials are valid")
    void shouldReturnAccessAndRefreshTokens_whenCredentialsValid() {
        // given
        LoginRequest request = new LoginRequest("user@email.com", "password");
        User user = User.of("tester", "user@email.com", "encoded-password");
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
    @DisplayName("returns new access token when refresh token is valid")
    void shouldReturnNewAccessToken_whenRefreshTokenValid() {
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
    @DisplayName("throws exception when refresh token is invalid")
    void shouldThrowException_whenRefreshTokenInvalid() {
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
    @DisplayName("throws exception when refresh token does not match stored token")
    void shouldThrowException_whenRefreshTokenMismatch() {
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
    @DisplayName("removes refresh token from store on logout")
    void shouldRemoveRefreshToken_whenLogoutCalled() {
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
    @DisplayName("deletes user and all related entities when user withdraws")
    void shouldDeleteUserAndAllRelations_whenUserWithdraws() {
        // given
        Long userId = 1L;
        User user = User.of("tester", "tester@email.com", "encodedPass");
        ReflectionTestUtils.setField(user, "id", userId);

        given(userQueryService.getUserById(userId)).willReturn(user);

        Map<Long, String> mockStore = new HashMap<>(Map.of(userId, "token"));
        ReflectionTestUtils.setField(authService, "refreshTokenStore", mockStore);

        // when
        authService.withdraw(userId);

        // then
        verify(userWithdrawalService).withdraw(user);

        Map<Long, String> store = (Map<Long, String>) ReflectionTestUtils.getField(authService, "refreshTokenStore");
        assertThat(store.containsKey(userId)).isFalse();
    }
}