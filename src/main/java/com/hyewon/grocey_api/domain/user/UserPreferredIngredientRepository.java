package com.hyewon.grocey_api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferredIngredientRepository extends JpaRepository<UserPreferredIngredient, Long> {
    void deleteByUser(User user);

}
