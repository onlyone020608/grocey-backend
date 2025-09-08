package com.hyewon.grocey_api.integration.user;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.user.dto.*;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.entity.UserAllergy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserController Integration Test")
@Sql(scripts = {
        "/sql/allergy-data.sql",
        "/sql/food-preference-data.sql",
        "/sql/preference-ingredient-data.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/users/me/summary - returns user summary when authenticated")
    void getUserSummary_withAuthenticatedUser_returnsSummary() throws Exception {
        // given
        var user = createTestUser("Mary Kim", "mary", "password123");
        String token = generateTokenFor(user);

        // when
        ResultActions result = mockMvc.perform(get("/api/users/me/summary")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getUsername()));
    }

    @Test
    @DisplayName("GET /api/users/me - returns user detail when authenticated")
    void getUserDetail_withAuthenticatedUser_returnsDetail() throws Exception {
        // given
        User user = createTestUser("Mary Kim", "mary", "password123");
        String token = generateTokenFor(user);

        // when & then
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("PATCH /api/users/me - updates user info when request is valid")
    void updateUserInfo_withValidRequest_updatesUserInfo() throws Exception {
        // given
        User user = createTestUser("Old Name", "old", "password123");
        String token = generateTokenFor(user);

        UserUpdateRequest request = UserUpdateRequest.builder()
                .userName("New Name")
                .email("new")
                .build();

        // when & then
        mockMvc.perform(patch("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getUsername()).isEqualTo("New Name");
        assertThat(updatedUser.getEmail()).isEqualTo("new");
    }

    @Test
    @DisplayName("PATCH /api/users/me/gender - updates user gender when request is valid")
    void updateGender_withValidRequest_updatesUserGender() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "pass123");
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

    @Test
    @DisplayName("PATCH /api/users/me/age-group - updates user age group when request is valid")
    void updateAgeGroup_withValidRequest_updatesUserAgeGroup() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "password123");
        String token = generateTokenFor(user);

        AgeGroupUpdateRequest request = AgeGroupUpdateRequest.builder()
                .ageValue(20)
                .build();

        // when & then
        mockMvc.perform(patch("/api/users/me/age-group")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getAgeGroup().name()).isEqualTo("TWENTIES");
    }

    @Test
    @DisplayName("PATCH /api/users/me/allergies - updates user allergies when request is valid")
    void updateUserAllergies_withValidRequest_updatesAllergies() throws Exception {
        // given
        User user = createTestUser("AllergyUser", "allergy", "password123");
        String token = generateTokenFor(user);
        UserAllergyUpdateRequest request = new UserAllergyUpdateRequest(List.of(1L, 2L));

        // when & then
        mockMvc.perform(patch("/api/users/me/allergies")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        List<UserAllergy> updated = userAllergyRepository.findByUser(user);
        assertThat(updated).hasSize(2);
        assertThat(updated).extracting(ua -> ua.getAllergy().getId())
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("PATCH /api/users/me/preferences - updates user preferences when request is valid")
    void updatePreferences_withValidRequest_updatesUserPreferences() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "password123");
        String token = generateTokenFor(user);

        PreferenceUpdateRequest request = PreferenceUpdateRequest.builder()
                .foodPreferenceIds(List.of(1L, 2L))
                .preferredIngredientIds(List.of(1L))
                .dislikedIngredientIds(List.of(2L))
                .build();

        // when & then
        mockMvc.perform(patch("/api/users/me/preferences")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getProfileCompleted()).isTrue();
    }

    @Test
    @DisplayName("PATCH /api/users/me/vegan - updates vegan status when request is valid")
    void updateVegan_withValidRequest_updatesVeganStatus() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "password123");
        String token = generateTokenFor(user);

        VeganUpdateRequest request = new VeganUpdateRequest(true);

        // when & then
        mockMvc.perform(patch("/api/users/me/vegan")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getVegan()).isTrue();
    }

    @Test
    @DisplayName("GET /api/users/me/status - returns profile completion status when authenticated")
    void getUserStatus_withAuthenticatedUser_returnsProfileCompletionStatus() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "password123");
        String token = generateTokenFor(user);

        User initial = userRepository.findById(user.getId()).orElseThrow();
        assertThat(initial.getProfileCompleted()).isFalse();

        // when
        ResultActions result = mockMvc.perform(get("/api/users/me/status")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.profileCompleted").value(false));
    }
}
