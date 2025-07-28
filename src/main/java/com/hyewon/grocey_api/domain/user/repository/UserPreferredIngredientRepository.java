package com.hyewon.grocey_api.domain.user.repository;

import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.entity.UserPreferredIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferredIngredientRepository extends JpaRepository<UserPreferredIngredient, Long> {
    void deleteByUser(User user);

}
