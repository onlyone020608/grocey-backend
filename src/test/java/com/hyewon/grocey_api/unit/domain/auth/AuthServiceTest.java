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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private UserQueryService userQueryService;
    @Mock private UserCommandService userCommandService;
    @Mock private FridgeCommandService fridgeCommandService;
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
        given(userQueryService.existsByEmail(signupRequest.email())).willReturn(false);
        given(ingredientQueryService.getIngredientById(1L)).willReturn(ingredient1);
        given(ingredientQueryService.getIngredientById(2L)).willReturn(ingredient2);
        given(ingredientQueryService.getIngredientById(3L)).willReturn(ingredient3);
        given(ingredientQueryService.getIngredientById(7L)).willReturn(ingredient7);
        given(ingredientQueryService.getIngredientById(8L)).willReturn(ingredient8);

        // when
        authService.signup(signupRequest);

        // then
        verify(userQueryService).existsByEmail(signupRequest.email());
        verify(fridgeCommandService).createFridge(any(Fridge.class));
    }

    @Test
    @DisplayName("throws exception when email is already in use during signup")
    void shouldThrowException_whenEmailAlreadyInUse() {
        // given
        given(userQueryService.existsByEmail(signupRequest.email())).willReturn(true);

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

        given(userQueryService.getUserByEmail(request.email())).willReturn(user);
        given(passwordEncoder.matches(request.password(), user.getPassword())).willReturn(true);
        given(jwtTokenProvider.generateAccessToken(1L)).willReturn("access-token");
        given(jwtTokenProvider.generateRefreshToken(1L)).willReturn("refresh-token");

        // when
        TokenResponse response = authService.login(request);

        // then
        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
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
        given(tokenService.validateRefreshToken(userId, oldRefreshToken)).willReturn(true);

        // when
        TokenResponse response = authService.refresh(request);

        // then
        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.refreshToken()).isEqualTo(oldRefreshToken);
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
        String requestToken = "invalid-refresh-token";

        TokenRefreshRequest request = TokenRefreshRequest.builder()
                .refreshToken(requestToken)
                .build();

        given(jwtTokenProvider.validateToken(requestToken)).willReturn(true);
        given(jwtTokenProvider.getUserIdFromToken(requestToken)).willReturn(userId);
        given(tokenService.validateRefreshToken(userId, requestToken)).willReturn(false);

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

        // when
        authService.logout(userId);

        // then
        verify(tokenService).deleteRefreshToken(userId);
    }
}