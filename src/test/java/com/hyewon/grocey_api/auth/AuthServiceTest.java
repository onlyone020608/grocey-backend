package com.hyewon.grocey_api.auth;

import com.hyewon.grocey_api.auth.dto.LoginRequest;
import com.hyewon.grocey_api.auth.dto.SignupRequest;
import com.hyewon.grocey_api.auth.dto.TokenRefreshRequest;
import com.hyewon.grocey_api.auth.dto.TokenResponse;
import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import com.hyewon.grocey_api.security.JwtTokenProvider;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private FridgeRepository fridgeRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest("tester", "tester@email.com", "securepass");
    }

    @Test
    @DisplayName("signup - signup - successful registration")
    void signup_shouldSucceed() {
        // given
        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(false);

        // when
        authService.signup(signupRequest);

        // then
        verify(fridgeRepository).save(any(Fridge.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("signup - throws exception when email is already in use")
    void signup_shouldThrowWhenEmailAlreadyExists() {
        // given
        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.signup(signupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");

        verify(userRepository, never()).save(any(User.class));
        verify(fridgeRepository, never()).save(any(Fridge.class));
    }

    @Test
    @DisplayName("login - returns access and refresh token when credentials are valid")
    void login_shouldReturnTokens_whenCredentialsAreValid() {
        // given
        LoginRequest request = new LoginRequest("user@email.com", "password");
        User user = new User("tester", "user@email.com", "password");
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByEmailAndPassword("user@email.com", "password")).willReturn(Optional.of(user));
        given(jwtTokenProvider.generateAccessToken(1L)).willReturn("access-token");
        given(jwtTokenProvider.generateRefreshToken(1L)).willReturn("refresh-token");

        // when
        TokenResponse response = authService.login(request);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("login - throws exception when credentials are invalid")
    void login_shouldThrowException_whenCredentialsInvalid() {
        // given
        LoginRequest request = new LoginRequest("wrong@email.com", "wrongpass");
        given(userRepository.findByEmailAndPassword(anyString(), anyString()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid credentials");
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
    @DisplayName("withdraw - deletes user and removes refresh token when user exists")
    void withdraw_shouldDeleteUserAndRemoveToken_whenUserExists() {
        // given
        Long userId = 1L;

        given(userRepository.existsById(userId)).willReturn(true);
        Map<Long, String> mockStore = new HashMap<>(Map.of(userId, "token"));
        ReflectionTestUtils.setField(authService, "refreshTokenStore", mockStore);

        // when
        authService.withdraw(userId);

        // then
        verify(userRepository).deleteById(userId);
        Map<Long, String> store = (Map<Long, String>) ReflectionTestUtils.getField(authService, "refreshTokenStore");
        assertThat(store.containsKey(userId)).isFalse();
    }

    @Test
    @DisplayName("withdraw - throws UserNotFoundException when user does not exist")
    void withdraw_shouldThrowException_whenUserNotFound() {
        // given
        Long userId = 99L;
        given(userRepository.existsById(userId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.withdraw(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("changePassword - should succeed when current password matches")
    void changePassword_shouldSucceed_whenPasswordMatches() {
        // given
        Long userId = 1L;
        String currentPassword = "oldPass";
        String newPassword = "newPass";
        String encodedPassword = "encodedNewPass";

        User user = new User("tester", "tester@email.com", "encodedOldPass");
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(currentPassword, "encodedOldPass")).willReturn(true);
        given(passwordEncoder.encode(newPassword)).willReturn(encodedPassword);

        // when
        authService.changePassword(userId, currentPassword, newPassword);

        // then
        verify(userRepository).save(user);
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
    }



    @Test
    @DisplayName("changePassword - throws exception when current password does not match")
    void changePassword_shouldThrow_whenPasswordIncorrect() {
        // given
        Long userId = 1L;
        User user = new User("tester", "tester@email.com", "encodedPass");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongPass", "encodedPass")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.changePassword(userId, "wrongPass", "newPass"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Current password does not match");

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    @DisplayName("changePassword - throws exception when user not found")
    void changePassword_shouldThrow_whenUserNotFound() {
        // given
        Long userId = 42L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.changePassword(userId, "irrelevant", "newPass"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }












}