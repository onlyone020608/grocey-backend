package com.hyewon.grocey_api.domain.user.service;

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

    @Transactional(readOnly = true)
    public UserSummaryResponse getUserSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return new UserSummaryResponse(user);
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return new UserDetailResponse(user);
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

        userAllergyRepository.deleteByUser(user);

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

        userFoodPreferenceRepository.deleteByUser(user);
        userPreferredIngredientRepository.deleteByUser(user);
        userDislikedIngredientRepository.deleteByUser(user);

        if (request.getFoodPreferenceIds() != null) {
            List<FoodPreference> foods = foodPreferenceRepository.findAllById(request.getFoodPreferenceIds());
            if (foods.size() != request.getFoodPreferenceIds().size()) {
                throw new InvalidRequestException("One or more food preference IDs are invalid.");
            }

            List<UserFoodPreference> userFoodPreferences = foods.stream()
                    .map(food -> UserFoodPreference.of(user, food))
                    .toList();
            userFoodPreferenceRepository.saveAll(userFoodPreferences);
        }

        if (request.getPreferredIngredientIds() != null) {
            List<Long> preferredIds = request.getPreferredIngredientIds();
            List<PreferenceIngredient> ingredients = preferenceIngredientRepository.findAllById(preferredIds);

            if (ingredients.size() != preferredIds.size()) {
                throw new InvalidRequestException("One or more preferred ingredient IDs are invalid.");
            }

            List<UserPreferredIngredient> preferredEntities = ingredients.stream()
                    .map(ingredient -> UserPreferredIngredient.of(user, ingredient))
                    .toList();
            userPreferredIngredientRepository.saveAll(preferredEntities);
        }

        if (request.getDislikedIngredientIds() != null) {
            List<Long> dislikedIds = request.getDislikedIngredientIds();
            List<PreferenceIngredient> dislikedIngredients = preferenceIngredientRepository.findAllById(dislikedIds);

            if (dislikedIngredients.size() != dislikedIds.size()) {
                throw new InvalidRequestException("One or more disliked ingredient IDs are invalid.");
            }

            List<UserDislikedIngredient> dislikedEntities = dislikedIngredients.stream()
                    .map(ingredient -> UserDislikedIngredient.of(user, ingredient))
                    .toList();
            userDislikedIngredientRepository.saveAll(dislikedEntities);
        }

        user.completeProfile();
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
}
