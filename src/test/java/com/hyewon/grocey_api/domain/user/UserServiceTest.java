package com.hyewon.grocey_api.domain.user;

import com.hyewon.grocey_api.domain.user.dto.*;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock private UserAllergyRepository userAllergyRepository;
    @Mock private AllergyRepository allergyRepository;
    @Mock private UserFoodPreferenceRepository userFoodPreferenceRepository;
    @Mock private UserDislikedIngredientRepository userDislikedIngredientRepository;
    @Mock private UserPreferredIngredientRepository userPreferredIngredientRepository;
    @Mock private FoodPreferenceRepository foodPreferenceRepository;
    @Mock private PreferenceIngredientRepository preferenceIngredientRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("tester", "tester@email.com", "pw", AgeGroup.TWENTIES, Gender.FEMALE);
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    @DisplayName("getUserSummary - returns summary info for existing user")
    void getUserSummary_shouldReturnSummary() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        UserSummaryDto result = userService.getUserSummary(1L);

        // then
        assertThat(result.getName()).isEqualTo("tester");
    }

    @Test
    @DisplayName("getUserSummary - throws exception if user not found")
    void getUserSummary_shouldThrowIfUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserSummary(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("getUserDetail - returns detailed info for existing user")
    void getUserDetail_shouldReturnDetail() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        UserDetailDto result = userService.getUserDetail(1L);

        // then
        assertThat(result.getUserName()).isEqualTo("tester");
        assertThat(result.getEmail()).isEqualTo("tester@email.com");
    }

    @Test
    @DisplayName("getUserDetail - throws exception if user not found")
    void getUserDetail_shouldThrowIfUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserDetail(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("updateUser - updates name and email when both are provided")
    void updateUser_shouldUpdateNameAndEmail() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserUpdateRequest request = new UserUpdateRequest();
        ReflectionTestUtils.setField(request, "userName", "newName");
        ReflectionTestUtils.setField(request, "email", "new@email.com");

        // when
        userService.updateUser(1L, request);

        // then
        assertThat(user.getUserName()).isEqualTo("newName");
        assertThat(user.getEmail()).isEqualTo("new@email.com");
    }

    @Test
    @DisplayName("updateUser - updates only email when name is null")
    void updateUser_shouldUpdateEmailOnly() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserUpdateRequest request = new UserUpdateRequest();
        ReflectionTestUtils.setField(request, "userName", null);
        ReflectionTestUtils.setField(request, "email", "updated@email.com");

        // when
        userService.updateUser(1L, request);

        // then
        assertThat(user.getUserName()).isEqualTo("tester"); // unchanged
        assertThat(user.getEmail()).isEqualTo("updated@email.com");
    }

    @Test
    @DisplayName("updateUser - throws exception when user not found")
    void updateUser_shouldThrowIfUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        UserUpdateRequest request = new UserUpdateRequest();
        ReflectionTestUtils.setField(request, "userName", "ignored");

        // when & then
        assertThatThrownBy(() -> userService.updateUser(1L, request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("updateGender - updates gender when valid value is provided")
    void updateGender_shouldUpdateGender() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        GenderUpdateRequest request = new GenderUpdateRequest();
        ReflectionTestUtils.setField(request, "gender", "male");

        // when
        userService.updateGender(1L, request);

        // then
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    @DisplayName("updateGender - throws exception for invalid gender value")
    void updateGender_shouldThrowForInvalidGender() {
        GenderUpdateRequest request = new GenderUpdateRequest();
        ReflectionTestUtils.setField(request, "gender", "invalid");

        assertThatThrownBy(() -> request.toEnum())
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Invalid gender value");
    }

    @Test
    @DisplayName("updateAgeGroup - updates age group when valid value is provided")
    void updateAgeGroup_shouldUpdateAgeGroup() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        AgeGroupUpdateRequest request = new AgeGroupUpdateRequest();
        ReflectionTestUtils.setField(request, "ageValue", 30);

        // when
        userService.updateAgeGroup(1L, request);

        // then
        assertThat(user.getAgeGroup()).isEqualTo(AgeGroup.THIRTIES);
    }

    @Test
    @DisplayName("updateAgeGroup - throws exception for invalid age group value")
    void updateAgeGroup_shouldThrowForInvalidAge() {
        AgeGroupUpdateRequest request = new AgeGroupUpdateRequest();
        ReflectionTestUtils.setField(request, "ageValue", 999); // 유효하지 않은 값

        assertThatThrownBy(() -> request.toEnum())
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Invalid age group value");
    }












}