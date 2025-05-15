package com.hyewon.grocey_api.integration.auth;

import com.hyewon.grocey_api.auth.dto.LoginRequest;
import com.hyewon.grocey_api.auth.dto.SignupRequest;
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
}
