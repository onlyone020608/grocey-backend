package com.hyewon.grocey_api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDislikedIngredientRepository extends JpaRepository<UserDislikedIngredient, Long> {
    void deleteByUser(User user);
}
