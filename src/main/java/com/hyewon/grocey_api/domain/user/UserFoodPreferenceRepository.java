package com.hyewon.grocey_api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFoodPreferenceRepository extends JpaRepository<UserFoodPreference, Long> {
    void deleteByUser(User user);
}
