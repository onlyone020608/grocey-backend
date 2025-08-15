package com.hyewon.grocey_api.unit.domain.user;

import com.hyewon.grocey_api.domain.user.dto.*;
import com.hyewon.grocey_api.domain.user.entity.*;
import com.hyewon.grocey_api.domain.user.repository.*;
import com.hyewon.grocey_api.domain.user.service.UserService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private UserAllergyRepository userAllergyRepository;
    @Mock private AllergyRepository allergyRepository;
    @Mock private UserFoodPreferenceRepository userFoodPreferenceRepository;
    @Mock private UserDislikedIngredientRepository userDislikedIngredientRepository;
    @Mock private UserPreferredIngredientRepository userPreferredIngredientRepository;
    @Mock private FoodPreferenceRepository foodPreferenceRepository;
    @Mock private PreferenceIngredientRepository preferenceIngredientRepository;
    @InjectMocks private UserService userService;

    private User user;
    private FoodPreference foodPreference;
    private Allergy allergy1;
    private Allergy allergy2;
    private PreferenceIngredient preferenceIngredient1;
    private PreferenceIngredient preferenceIngredient2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("tester")
                .email("tester@email.com")
                .password("pw")
                .gender(Gender.FEMALE)
                .ageGroup(AgeGroup.TWENTIES)
                .build();
        foodPreference = FoodPreference.builder()
                .id(10L)
                .name("Meat")
                .build();
        allergy1 = Allergy.builder()
                .id(10L)
                .name("Egg")
                .build();
        allergy2 = Allergy.builder()
                .id(20L)
                .name("Milk")
                .build();
        preferenceIngredient1 = PreferenceIngredient.builder()
                .id(20L)
                .name("Garlic")
                .build();
        preferenceIngredient2 = PreferenceIngredient.builder()
                .id(30L)
                .name("Cucumber")
                .build();
    }

    @Test
    @DisplayName("getUserSummary - returns summary info for existing user")
    void getUserSummary_shouldReturnSummary() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        UserSummaryResponse result = userService.getUserSummary(1L);

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
        UserDetailResponse result = userService.getUserDetail(1L);

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

        UserUpdateRequest request = new UserUpdateRequest("newName", "new@email.com");

        // when
        userService.updateUser(1L, request);

        // then
        assertThat(user.getUsername()).isEqualTo("newName");
        assertThat(user.getEmail()).isEqualTo("new@email.com");
    }

    @Test
    @DisplayName("updateUser - updates only email when name is null")
    void updateUser_shouldUpdateEmailOnly() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserUpdateRequest request = new UserUpdateRequest(null, "updated@email.com");

        // when
        userService.updateUser(1L, request);

        // then
        assertThat(user.getUsername()).isEqualTo("tester"); // unchanged
        assertThat(user.getEmail()).isEqualTo("updated@email.com");
    }

    @Test
    @DisplayName("updateUser - throws exception when user not found")
    void updateUser_shouldThrowIfUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        UserUpdateRequest request = new UserUpdateRequest("ignored", "new@email.con");
        // when & then
        assertThatThrownBy(() -> userService.updateUser(1L, request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("updateGender - updates gender when valid value is provided")
    void updateGender_shouldUpdateGender() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        GenderUpdateRequest request = new GenderUpdateRequest("male");

        // when
        userService.updateGender(1L, request);

        // then
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    @DisplayName("updateGender - throws exception for invalid gender value")
    void updateGender_shouldThrowForInvalidGender() {
        // given
        GenderUpdateRequest request = new GenderUpdateRequest("invalid");

        // when & then
        assertThatThrownBy(() -> request.toEnum())
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Invalid gender value");
    }

    @Test
    @DisplayName("updateAgeGroup - updates age group when valid value is provided")
    void updateAgeGroup_shouldUpdateAgeGroup() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        AgeGroupUpdateRequest request = new AgeGroupUpdateRequest(30);

        // when
        userService.updateAgeGroup(1L, request);

        // then
        assertThat(user.getAgeGroup()).isEqualTo(AgeGroup.THIRTIES);
    }

    @Test
    @DisplayName("updateAgeGroup - throws exception for invalid age group value")
    void updateAgeGroup_shouldThrowForInvalidAge() {
        // given
        AgeGroupUpdateRequest request = new AgeGroupUpdateRequest(999);

        //when & then
        assertThatThrownBy(() -> request.toEnum())
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Invalid age group value");
    }

    @Test
    @DisplayName("updateUserAllergies - updates user allergies successfully")
    void updateUserAllergies_shouldUpdateCorrectly() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(allergyRepository.findAllById(List.of(10L, 20L))).willReturn(List.of(allergy1, allergy2));
        UserAllergyUpdateRequest request = new UserAllergyUpdateRequest(List.of(10L, 20L));

        // when
        userService.updateUserAllergies(1L, request);

        // then
        verify(userAllergyRepository).deleteByUser(user);
        verify(userAllergyRepository).saveAll(argThat(allergies ->
                StreamSupport.stream(allergies.spliterator(), false)
                        .count() == 2
        ));
    }

    @Test
    @DisplayName("updateUserAllergies - throws when allergy ID is invalid")
    void updateUserAllergies_shouldThrowIfAllergyIdInvalid() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(allergyRepository.findAllById(List.of(10L, 999L))).willReturn(List.of(allergy1));
        UserAllergyUpdateRequest request = new UserAllergyUpdateRequest(List.of(10L, 999L));

        // when & then
        assertThatThrownBy(() -> userService.updateUserAllergies(1L, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("One or more allergy IDs are invalid.");
    }

    @Test
    @DisplayName("updateUserPreferences - updates preferences when all IDs are valid")
    void updateUserPreferences_shouldUpdateAll() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        PreferenceUpdateRequest request = new PreferenceUpdateRequest(
                List.of(10L),
                List.of(20L),
                List.of(30L)
        );
        given(foodPreferenceRepository.findAllById(List.of(10L))).willReturn(List.of(foodPreference));
        given(preferenceIngredientRepository.findAllById(List.of(20L))).willReturn(List.of(preferenceIngredient1));
        given(preferenceIngredientRepository.findAllById(List.of(30L))).willReturn(List.of(preferenceIngredient2));

        // when
        userService.updateUserPreferences(1L, request);

        // then
        verify(userFoodPreferenceRepository).saveAll(any());
        verify(userPreferredIngredientRepository).saveAll(any());
        verify(userDislikedIngredientRepository).saveAll(any());
        assertThat(user.getProfileCompleted()).isTrue();
    }

    @Test
    @DisplayName("updateUserPreferences - throws if foodPreferenceIds are invalid")
    void updateUserPreferences_shouldThrowForInvalidFoodPreference() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(foodPreferenceRepository.findAllById(List.of(10L, 99L))).willReturn(List.of(foodPreference));
        PreferenceUpdateRequest request = new PreferenceUpdateRequest(List.of(10L, 99L), List.of(), List.of());

        // when & then
        assertThatThrownBy(() -> userService.updateUserPreferences(1L, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("food preference");
    }

    @Test
    @DisplayName("updateUserPreferences - throws if preferredIngredientIds are invalid")
    void updateUserPreferences_shouldThrowForInvalidPreferredIngredient() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(preferenceIngredientRepository.findAllById(List.of(20L, 99L))).willReturn(List.of(preferenceIngredient1));

        PreferenceUpdateRequest request = new PreferenceUpdateRequest();
        ReflectionTestUtils.setField(request, "preferredIngredientIds", List.of(20L, 99L));

        // when & then
        assertThatThrownBy(() -> userService.updateUserPreferences(1L, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("preferred ingredient");
    }

    @Test
    @DisplayName("updateUserPreferences - throws if dislikedIngredientIds are invalid")
    void updateUserPreferences_shouldThrowForInvalidDislikedIngredient() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(preferenceIngredientRepository.findAllById(List.of(20L, 999L))).willReturn(List.of(preferenceIngredient1));

        PreferenceUpdateRequest request = new PreferenceUpdateRequest();
        ReflectionTestUtils.setField(request, "dislikedIngredientIds", List.of(20L, 999L));

        // when & then
        assertThatThrownBy(() -> userService.updateUserPreferences(1L, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("disliked ingredient");
    }

    @Test
    @DisplayName("updateVeganStatus - updates isVegan flag correctly")
    void updateVeganStatus_shouldUpdateIsVegan() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        VeganUpdateRequest request = new VeganUpdateRequest(true);

        // when
        userService.updateVeganStatus(1L, request);

        // then
        assertThat(user.getVegan()).isTrue();
    }

    @Test
    @DisplayName("updateVeganStatus - throws exception if user not found")
    void updateVeganStatus_shouldThrowIfUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        VeganUpdateRequest request = new VeganUpdateRequest(true);

        // when & then
        assertThatThrownBy(() -> userService.updateVeganStatus(1L, request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("checkProfileCompletion - returns true if profile is completed")
    void checkProfileCompletion_shouldReturnTrueIfCompleted() {
        // given
        user.completeProfile();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        boolean result = userService.checkProfileCompletion(1L);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("checkProfileCompletion - returns false if profile is not completed")
    void checkProfileCompletion_shouldReturnFalseIfNotCompleted() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        boolean result = userService.checkProfileCompletion(1L);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("checkProfileCompletion - throws exception if user not found")
    void checkProfileCompletion_shouldThrowIfUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.checkProfileCompletion(1L))
                .isInstanceOf(UserNotFoundException.class);
    }
}