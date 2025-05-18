package com.hyewon.grocey_api.domain.user;

import com.hyewon.grocey_api.domain.user.dto.*;
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

    public UserSummaryDto getUserSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return new UserSummaryDto(user);
    }

    public UserDetailDto getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return new UserDetailDto(user);
    }

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

    public void updateGender(Long userId, GenderUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.updateGender(request.toEnum());
    }

    public void updateAgeGroup(Long userId, AgeGroupUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.updateAgeGroup(request.toEnum());
    }

    public void updateUserAllergies(Long userId, UserAllergyUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 기존 알러지 삭제
        userAllergyRepository.deleteByUser(user);

        List<Long> allergyIds = request.getAllergyIds();
        List<Allergy> allergies = allergyRepository.findAllById(allergyIds);
        if (allergies.size() != allergyIds.size()) {
            throw new InvalidRequestException("One or more allergy IDs are invalid.");
        }

        List<UserAllergy> newUserAllergies = allergies.stream()
                .map(allergy -> new UserAllergy(user, allergy))
                .toList();
        userAllergyRepository.saveAll(newUserAllergies);
    }

    public void updateUserPreferences(Long userId, PreferenceUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 1. 기존 데이터 삭제
        userFoodPreferenceRepository.deleteByUser(user);
        userPreferredIngredientRepository.deleteByUser(user);
        userDislikedIngredientRepository.deleteByUser(user);

        // 2. 선호 음식
        if (request.getFoodPreferenceIds() != null) {
            List<FoodPreference> foods = foodPreferenceRepository.findAllById(request.getFoodPreferenceIds());
            if (foods.size() != request.getFoodPreferenceIds().size()) {
                throw new InvalidRequestException("One or more food preference IDs are invalid.");
            }
            List<UserFoodPreference> userFoodPreferences = foods.stream()
                    .map(food -> new UserFoodPreference(user, food))
                    .toList();
            userFoodPreferenceRepository.saveAll(userFoodPreferences);
        }

        // 3. 선호 식재료
        if (request.getPreferredIngredientIds() != null) {
            List<Long> preferredIds = request.getPreferredIngredientIds();
            List<PreferenceIngredient> ingredients = preferenceIngredientRepository.findAllById(preferredIds);
            if (ingredients.size() != preferredIds.size()) {
                throw new InvalidRequestException("One or more preferred ingredient IDs are invalid.");
            }
            List<UserPreferredIngredient> preferredEntities = ingredients.stream()
                    .map(ingredient -> new UserPreferredIngredient(user, ingredient))
                    .toList();
            userPreferredIngredientRepository.saveAll(preferredEntities);
        }

        // 4. 비선호 식재료
        if (request.getDislikedIngredientIds() != null) {
            List<Long> dislikedIds = request.getDislikedIngredientIds();
            List<PreferenceIngredient> dislikedIngredients = preferenceIngredientRepository.findAllById(dislikedIds);
            if (dislikedIngredients.size() != dislikedIds.size()) {
                throw new InvalidRequestException("One or more disliked ingredient IDs are invalid.");
            }
            List<UserDislikedIngredient> dislikedEntities = dislikedIngredients.stream()
                    .map(ingredient -> new UserDislikedIngredient(user, ingredient))
                    .toList();
            userDislikedIngredientRepository.saveAll(dislikedEntities);
        }
    }

    public void updateVeganStatus(Long userId, VeganUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateVeganStatus(request.isVegan());
    }


}
