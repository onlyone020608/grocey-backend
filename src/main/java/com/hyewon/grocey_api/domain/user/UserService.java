package com.hyewon.grocey_api.domain.user;

import com.hyewon.grocey_api.domain.user.dto.*;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

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


}
