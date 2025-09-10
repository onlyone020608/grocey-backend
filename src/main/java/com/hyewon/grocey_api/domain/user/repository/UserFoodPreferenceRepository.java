package com.hyewon.grocey_api.domain.user.repository;

import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.entity.UserFoodPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFoodPreferenceRepository extends JpaRepository<UserFoodPreference, Long> {
    void deleteByUser(User user);
    void deleteAllByUserId(Long userId);
}
