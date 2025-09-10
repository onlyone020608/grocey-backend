package com.hyewon.grocey_api.domain.user.service;

import com.hyewon.grocey_api.domain.auth.service.TokenService;
import com.hyewon.grocey_api.domain.user.dto.*;
import com.hyewon.grocey_api.domain.user.entity.*;
import com.hyewon.grocey_api.domain.user.repository.*;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserAllergyRepository userAllergyRepository;
    private final AllergyRepository allergyRepository;
    private final UserFoodPreferenceRepository userFoodPreferenceRepository;
    private final UserDislikedIngredientRepository userDislikedIngredientRepository;
    private final UserPreferredIngredientRepository userPreferredIngredientRepository;
    private final FoodPreferenceRepository foodPreferenceRepository;
    private final PreferenceIngredientRepository preferenceIngredientRepository;
    private final UserWithdrawalService userWithdrawalService;
    private final TokenService tokenService;


    @Transactional(readOnly = true)
    public UserSummaryResponse getUserSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return UserSummaryResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return UserDetailResponse.from(user);
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (request.getUserName() != null) {
            user.updateName(request.getUserName());
        }

        if (request.getEmail() != null) {
            user.updateEmail(request.getEmail());
        }
    }
    @Transactional
    public void updateGender(Long userId, GenderUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.updateGender(request.toEnum());
    }

    @Transactional
    public void updateAgeGroup(Long userId, AgeGroupUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.updateAgeGroup(request.toEnum());
    }

    @Transactional
    public void updateUserAllergies(Long userId, UserAllergyUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userAllergyRepository.deleteAllByUserId(userId);

        List<Long> allergyIds = request.getAllergyIds();
        List<Allergy> allergies = allergyRepository.findAllById(allergyIds);

        if (allergies.size() != allergyIds.size()) {
            throw new InvalidRequestException("One or more allergy IDs are invalid.");
        }

        List<UserAllergy> newUserAllergies = allergies.stream()
                .map(allergy -> UserAllergy.of(user, allergy))
                .toList();
        userAllergyRepository.saveAll(newUserAllergies);
    }

    @Transactional
    public void updateUserPreferences(Long userId, PreferenceUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userFoodPreferenceRepository.deleteAllByUserId(userId);
        userPreferredIngredientRepository.deleteAllByUserId(userId);
        userDislikedIngredientRepository.deleteAllByUserId(userId);

        saveUserFoodPreferences(user, request.getFoodPreferenceIds());
        saveUserPreferredIngredients(user, request.getPreferredIngredientIds());
        saveUserDislikedIngredients(user, request.getDislikedIngredientIds());

        user.completeProfile();
    }

    private void saveUserFoodPreferences(User user, List<Long> foodIds) {
        if (foodIds == null) return;

        List<FoodPreference> foods = foodPreferenceRepository.findAllById(foodIds);
        if (foods.size() != foodIds.size()) {
            throw new InvalidRequestException("One or more food preference IDs are invalid.");
        }

        List<UserFoodPreference> entities = foods.stream()
                .map(food -> UserFoodPreference.of(user, food))
                .toList();
        userFoodPreferenceRepository.saveAll(entities);
    }

    private void saveUserPreferredIngredients(User user, List<Long> ingredientIds) {
        if (ingredientIds == null) return;

        List<PreferenceIngredient> ingredients = preferenceIngredientRepository.findAllById(ingredientIds);
        if (ingredients.size() != ingredientIds.size()) {
            throw new InvalidRequestException("One or more preferred ingredient IDs are invalid.");
        }

        List<UserPreferredIngredient> entities = ingredients.stream()
                .map(ingredient -> UserPreferredIngredient.of(user, ingredient))
                .toList();
        userPreferredIngredientRepository.saveAll(entities);
    }

    private void saveUserDislikedIngredients(User user, List<Long> ingredientIds) {
        if (ingredientIds == null) return;

        List<PreferenceIngredient> ingredients = preferenceIngredientRepository.findAllById(ingredientIds);
        if (ingredients.size() != ingredientIds.size()) {
            throw new InvalidRequestException("One or more disliked ingredient IDs are invalid.");
        }

        List<UserDislikedIngredient> entities = ingredients.stream()
                .map(ingredient -> UserDislikedIngredient.of(user, ingredient))
                .toList();
        userDislikedIngredientRepository.saveAll(entities);
    }

    @Transactional
    public void updateVeganStatus(Long userId, VeganUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateVeganStatus(request.isVegan());
    }

    @Transactional(readOnly = true)
    public boolean checkProfileCompletion(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return Boolean.TRUE.equals(user.getProfileCompleted());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        tokenService.deleteRefreshToken(userId);
        userWithdrawalService.withdraw(user);
    }
}
