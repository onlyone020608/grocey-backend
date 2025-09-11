package com.hyewon.grocey_api.unit.domain.user;

import com.hyewon.grocey_api.domain.auth.service.TokenService;
import com.hyewon.grocey_api.domain.user.dto.*;
import com.hyewon.grocey_api.domain.user.entity.*;
import com.hyewon.grocey_api.domain.user.repository.*;
import com.hyewon.grocey_api.domain.user.service.UserService;
import com.hyewon.grocey_api.domain.user.service.UserWithdrawalService;
import com.hyewon.grocey_api.fixture.UserFixture;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Mock private UserWithdrawalService userWithdrawalService;
    @Mock private TokenService tokenService;
    @InjectMocks private UserService userService;

    private User user;
    private FoodPreference foodPreference;
    private Allergy allergy1;
    private Allergy allergy2;
    private PreferenceIngredient preferenceIngredient1;
    private PreferenceIngredient preferenceIngredient2;

    @BeforeEach
    void setUp() {
        user = UserFixture.aDefaultUser();
        foodPreference = FoodPreference.builder()
                .name("Meat")
                .build();
        allergy1 = Allergy.builder()
                .name("Egg")
                .build();
        allergy2 = Allergy.builder()
                .name("Milk")
                .build();
        preferenceIngredient1 = PreferenceIngredient.builder()
                .name("Garlic")
                .build();
        preferenceIngredient2 = PreferenceIngredient.builder()
                .name("Cucumber")
                .build();
    }

    @Test
    @DisplayName("returns summary info when user exists")
    void shouldReturnUserSummary_whenUserExists() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        UserSummaryResponse result = userService.getUserSummary(userId);

        // then
        assertThat(result.name()).isEqualTo("tester");
    }

    @Test
    @DisplayName("throws UserNotFoundException when user does not exist in summary lookup")
    void shouldThrowException_whenUserNotFoundInSummary() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class,
                () ->  userService.getUserSummary(userId));
    }

    @Test
    @DisplayName("returns detailed info when user exists")
    void shouldReturnUserDetail_whenUserExists() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        UserDetailResponse result = userService.getUserDetail(userId);

        // then
        assertThat(result.userName()).isEqualTo("tester");
        assertThat(result.email()).isEqualTo("tester@email.com");
    }

    @Test
    @DisplayName("throws UserNotFoundException when user does not exist in detail lookup")
    void shouldThrowException_whenUserNotFoundInDetail() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class,
                () ->  userService.getUserDetail(userId));
    }

    @Test
    @DisplayName("updates name and email when both provided")
    void shouldUpdateUserNameAndEmail_whenBothProvided() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        UserUpdateRequest request = new UserUpdateRequest("newName", "new@email.com");

        // when
        userService.updateUser(userId, request);

        // then
        assertThat(user.getUsername()).isEqualTo("newName");
        assertThat(user.getEmail()).isEqualTo("new@email.com");
    }

    @Test
    @DisplayName("updates only email when name is null")
    void shouldUpdateEmailOnly_whenNameIsNull() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        UserUpdateRequest request = new UserUpdateRequest(null, "updated@email.com");

        // when
        userService.updateUser(userId, request);

        // then
        assertThat(user.getUsername()).isEqualTo("tester");
        assertThat(user.getEmail()).isEqualTo("updated@email.com");
    }

    @Test
    @DisplayName("throws UserNotFoundException when updating user that does not exist")
    void shouldThrowException_whenUserNotFoundOnUpdate() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        UserUpdateRequest request = new UserUpdateRequest("ignored", "new@email.com");

        // when & then
        assertThrows(UserNotFoundException.class,
                () ->  userService.updateUser(userId, request));
    }

    @Test
    @DisplayName("updates gender when valid value is provided")
    void shouldUpdateGender_whenValidValueProvided() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        GenderUpdateRequest request = new GenderUpdateRequest("male");

        // when
        userService.updateGender(userId, request);

        // then
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    @DisplayName("throws InvalidRequestException when gender value is invalid")
    void shouldThrowException_whenGenderValueInvalid() {
        // given
        GenderUpdateRequest request = new GenderUpdateRequest("invalid");

        // when & then
        assertThatThrownBy(() -> request.toEnum())
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Invalid gender value");
    }

    @Test
    @DisplayName("updates age group when valid value is provided")
    void shouldUpdateAgeGroup_whenValidValueProvided() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        AgeGroupUpdateRequest request = new AgeGroupUpdateRequest(30);

        // when
        userService.updateAgeGroup(userId, request);

        // then
        assertThat(user.getAgeGroup()).isEqualTo(AgeGroup.THIRTIES);
    }

    @Test
    @DisplayName("throws InvalidRequestException when age group value is invalid")
    void shouldThrowException_whenAgeGroupValueInvalid() {
        // given
        AgeGroupUpdateRequest request = new AgeGroupUpdateRequest(999);

        //when & then
        assertThatThrownBy(() -> request.toEnum())
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Invalid age group value");
    }

    @Test
    @DisplayName("updates user allergies when valid IDs provided")
    void shouldUpdateUserAllergies_whenValidIdsProvided() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(allergyRepository.findAllById(List.of(10L, 20L))).willReturn(List.of(allergy1, allergy2));
        UserAllergyUpdateRequest request = new UserAllergyUpdateRequest(List.of(10L, 20L));

        // when
        userService.updateUserAllergies(userId, request);

        // then
        verify(userAllergyRepository).deleteAllByUserId(userId);
        verify(userAllergyRepository).saveAll(argThat(allergies ->
                StreamSupport.stream(allergies.spliterator(), false)
                        .count() == 2
        ));
    }

    @Test
    @DisplayName("throws InvalidRequestException when allergy IDs are invalid")
    void shouldThrowException_whenAllergyIdsInvalid() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(allergyRepository.findAllById(List.of(10L, 999L))).willReturn(List.of(allergy1));
        UserAllergyUpdateRequest request = new UserAllergyUpdateRequest(List.of(10L, 999L));

        // when & then
        assertThatThrownBy(() -> userService.updateUserAllergies(userId, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("One or more allergy IDs are invalid.");
    }

    @Test
    @DisplayName("updates user preferences when all IDs are valid")
    void shouldUpdateUserPreferences_whenAllIdsValid() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        PreferenceUpdateRequest request = new PreferenceUpdateRequest(
                List.of(10L),
                List.of(20L),
                List.of(30L)
        );
        given(foodPreferenceRepository.findAllById(List.of(10L))).willReturn(List.of(foodPreference));
        given(preferenceIngredientRepository.findAllById(List.of(20L))).willReturn(List.of(preferenceIngredient1));
        given(preferenceIngredientRepository.findAllById(List.of(30L))).willReturn(List.of(preferenceIngredient2));

        // when
        userService.updateUserPreferences(userId, request);

        // then
        verify(userFoodPreferenceRepository).saveAll(any());
        verify(userPreferredIngredientRepository).saveAll(any());
        verify(userDislikedIngredientRepository).saveAll(any());
        assertThat(user.getProfileCompleted()).isTrue();
    }

    @Test
    @DisplayName("throws InvalidRequestException when foodPreferenceIds are invalid")
    void shouldThrowException_whenFoodPreferenceIdsInvalid() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(foodPreferenceRepository.findAllById(List.of(10L, 99L))).willReturn(List.of(foodPreference));
        PreferenceUpdateRequest request = new PreferenceUpdateRequest(List.of(10L, 99L), List.of(), List.of());

        // when & then
        assertThatThrownBy(() -> userService.updateUserPreferences(userId, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("food preference");
    }

    @Test
    @DisplayName("throws InvalidRequestException when preferredIngredientIds are invalid")
    void shouldThrowException_whenPreferredIngredientIdsInvalid() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(preferenceIngredientRepository.findAllById(List.of(20L, 99L))).willReturn(List.of(preferenceIngredient1));

        PreferenceUpdateRequest request = PreferenceUpdateRequest.builder()
                .preferredIngredientIds(List.of(20L, 99L))
                .build();

        // when & then
        assertThatThrownBy(() -> userService.updateUserPreferences(userId, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("preferred ingredient");
    }

    @Test
    @DisplayName("throws InvalidRequestException when dislikedIngredientIds are invalid")
    void shouldThrowException_whenDislikedIngredientIdsInvalid() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(preferenceIngredientRepository.findAllById(List.of(20L, 999L))).willReturn(List.of(preferenceIngredient1));

        PreferenceUpdateRequest request = PreferenceUpdateRequest.builder()
                .dislikedIngredientIds(List.of(20L, 999L))
                .build();

        // when & then
        assertThatThrownBy(() -> userService.updateUserPreferences(userId, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("disliked ingredient");
    }

    @Test
    @DisplayName("updates vegan status when valid request is provided")
    void shouldUpdateVeganStatus_whenValidRequestProvided() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        VeganUpdateRequest request = new VeganUpdateRequest(true);

        // when
        userService.updateVeganStatus(userId, request);

        // then
        assertThat(user.getVegan()).isTrue();
    }

    @Test
    @DisplayName("throws UserNotFoundException when updating vegan status of non-existing user")
    void shouldThrowException_whenUserNotFoundOnUpdateVeganStatus() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        VeganUpdateRequest request = new VeganUpdateRequest(true);

        // when & then
        assertThrows(UserNotFoundException.class,
                () ->  userService.updateVeganStatus(userId, request));
    }

    @Test
    @DisplayName("returns true when profile is completed")
    void shouldReturnTrue_whenProfileCompleted() {
        // given
        Long userId = 1L;
        user.completeProfile();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        boolean result = userService.checkProfileCompletion(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("returns false when profile is not completed")
    void shouldReturnFalse_whenProfileNotCompleted() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        boolean result = userService.checkProfileCompletion(userId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("throws UserNotFoundException when checking profile completion of non-existing user")
    void shouldThrowException_whenUserNotFoundOnCheckProfileCompletion() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class,
                () ->  userService.checkProfileCompletion(userId));
    }

    @Test
    @DisplayName("deletes user and all related entities when user withdraws")
    void shouldDeleteUserAndAllRelations_whenUserWithdraws() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        userService.deleteUser(userId);

        // then
        verify(userWithdrawalService).withdraw(user);
    }
}