package com.hyewon.grocey_api.integration.auth;

import com.hyewon.grocey_api.auth.dto.LoginRequest;
import com.hyewon.grocey_api.auth.dto.SignupRequest;
import com.hyewon.grocey_api.auth.dto.TokenRefreshRequest;
import com.hyewon.grocey_api.auth.dto.TokenResponse;
import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("AuthController Integration Test")
public class AuthControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("POST /api/auth/signup - should register new user")
    void signup_shouldSucceed() throws Exception {
        SignupRequest request = new SignupRequest("mary@example.com", "securepw", "Mary");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/auth/login - should return tokens for valid credentials")
    void login_shouldReturnTokens() throws Exception {
        createTestUser("TestUser", "login@example.com", "test1234!");


        LoginRequest request = new LoginRequest("login@example.com", "test1234!");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("POST /api/auth/refresh - should return new tokens")
    void refresh_shouldReturnNewTokens() throws Exception {
        createTestUser("RefreshUser", "refresh@example.com", "test1234!");


        LoginRequest loginRequest = new LoginRequest("refresh@example.com", "test1234!");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        TokenResponse tokens = objectMapper.readValue(loginResponse, TokenResponse.class);


        TokenRefreshRequest refreshRequest = TokenRefreshRequest.builder()
                .refreshToken(tokens.getRefreshToken())
                .build();

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("POST /api/auth/logout - should succeed with valid token and invalidate refresh token")
    void logout_shouldInvalidateRefreshToken() throws Exception {
        createTestUser("LogoutUser", "logout@example.com", "test1234!");

        LoginRequest loginRequest = new LoginRequest("logout@example.com", "test1234!");
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        TokenResponse tokens = objectMapper.readValue(loginResponse, TokenResponse.class);
        String accessToken = tokens.getAccessToken();

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("DELETE /api/auth/withdraw - should delete user and invalidate refresh token")
    void withdraw_shouldDeleteUserAndInvalidateToken() throws Exception {
        createTestUser("WithdrawUser", "withdraw@example.com", "test1234!");

        LoginRequest loginRequest = new LoginRequest("withdraw@example.com", "test1234!");
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        TokenResponse tokens = objectMapper.readValue(loginResponse, TokenResponse.class);
        String accessToken = tokens.getAccessToken();

        mockMvc.perform(delete("/api/auth/withdraw")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());
    }


}
