package com.hyewon.grocey_api.domain.user.repository;

import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.entity.UserDislikedIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDislikedIngredientRepository extends JpaRepository<UserDislikedIngredient, Long> {
    void deleteByUser(User user);
    void deleteAllByUserId(Long userId);
}
