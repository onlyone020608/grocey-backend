package com.hyewon.grocey_api.integration.auth;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.auth.dto.LoginRequest;
import com.hyewon.grocey_api.domain.auth.dto.SignupRequest;
import com.hyewon.grocey_api.domain.auth.dto.TokenRefreshRequest;
import com.hyewon.grocey_api.domain.auth.dto.TokenResponse;
import com.hyewon.grocey_api.domain.auth.repository.RefreshTokenRepository;
import com.hyewon.grocey_api.domain.auth.service.TokenService;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AuthController Integration Test")
public class AuthControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenService tokenService;

    @MockitoBean
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("POST /api/auth/signup - registers a new user when request is valid")
    void signUp_withValidRequest_registersNewUser() throws Exception {
        // given
        SignupRequest request = new SignupRequest("mary@example.com", "securepw", "Mary");

        // when
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // then
        Optional<User> userOpt = userRepository.findByEmail("mary@example.com");
        assertThat(userOpt).isPresent();

        User user = userOpt.get();
        assertThat(user.getUsername()).isEqualTo("Mary");
        assertThat(passwordEncoder.matches("securepw", user.getPassword())).isTrue();
    }

    @Test
    @DisplayName("POST /api/auth/login - returns tokens when credentials are valid")
    void login_withValidCredentials_returnsTokens() throws Exception {
        // given
        User user = createTestUser();

        LoginRequest request = new LoginRequest(user.getEmail(), "password");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("POST /api/auth/refresh - returns new tokens when refresh token is valid")
    void refresh_withValidRefreshToken_returnsNewTokens() throws Exception {
        // given
        User user = createTestUser();

        LoginRequest loginRequest = new LoginRequest(user.getEmail(), "password");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        TokenResponse tokens = objectMapper.readValue(loginResponse, TokenResponse.class);

        given(refreshTokenRepository.findByUserId(user.getId()))
                .willReturn(tokens.refreshToken());

        TokenRefreshRequest refreshRequest = TokenRefreshRequest.builder()
                .refreshToken(tokens.refreshToken())
                .build();

        // when & then
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("POST /api/auth/logout - invalidates refresh token when request is valid")
    void logout_withValidToken_invalidatesRefreshToken() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);

        // when & then
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/auth/password - changes password when current password is correct")
    void changePassword_withValidCurrentPassword_updatesPassword() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);

        // when
        mockMvc.perform(patch("/api/auth/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "currentPassword": "password",
                          "newPassword": "newPassword456"
                        }
                    """))
                .andExpect(status().isOk());

        // then
        LoginRequest newLogin = new LoginRequest(user.getEmail(), "newPassword456");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newLogin)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/auth/password - fails when current password is incorrect")
    void changePassword_withInvalidCurrentPassword_returnsBadRequest() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);

        // when & then
        mockMvc.perform(patch("/api/auth/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "currentPassword": "wrongPassword",
                          "newPassword": "newPassword456"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }
}
