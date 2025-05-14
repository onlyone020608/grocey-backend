package com.hyewon.grocey_api.integration.user;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.dto.GenderUpdateRequest;
import com.hyewon.grocey_api.domain.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("UserController Integration Test")
public class UserControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/users/me/summary - Should return user summary")
    void getUserSummary_shouldReturnUserSummary() throws Exception {
        // given
        var user = createTestUser("Mary Kim", "mary@example.com", "password123");
        String token = generateTokenFor(user);

        // when
        ResultActions result = mockMvc.perform(get("/api/users/me/summary")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getUserName()));
    }

    @Test
    @DisplayName("GET /api/users/me - should return user detail")
    void getUserDetail_shouldReturnUserDetail() throws Exception {
        // given
        User user = createTestUser("Mary Kim", "mary@example.com", "password123");
        String token = generateTokenFor(user);

        // when & then
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(user.getUserName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("PATCH /api/users/me - should update user info")
    void updateUserInfo_shouldUpdateUserNameAndEmail() throws Exception {
        // given
        User user = createTestUser("Old Name", "old@example.com", "password123");
        String token = generateTokenFor(user);

        UserUpdateRequest request = UserUpdateRequest.builder()
                .userName("New Name")
                .email("new@example.com")
                .build();

        // when & then
        mockMvc.perform(patch("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getUserName()).isEqualTo("New Name");
        assertThat(updatedUser.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    @DisplayName("PATCH /api/users/me/gender - should update user gender")
    void updateGender_shouldUpdateUserGender() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@example.com", "pass123");
        String token = generateTokenFor(user);

        GenderUpdateRequest request = GenderUpdateRequest.builder()
                .gender("FEMALE")
                .build();

        // when & then
        mockMvc.perform(patch("/api/users/me/gender")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getGender().name()).isEqualTo("FEMALE");
    }


}
