package com.hyewon.grocey_api.domain.user;

import com.hyewon.grocey_api.domain.user.dto.*;
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

        // 새 알러지 설정
        List<Allergy> allergies = allergyRepository.findAllById(request.getAllergyIds());
        List<UserAllergy> newUserAllergies = allergies.stream()
                .map(allergy -> new UserAllergy(user, allergy))
                .toList();

        userAllergyRepository.saveAll(newUserAllergies);
    }


}
