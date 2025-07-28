package com.hyewon.grocey_api.domain.user.repository;

import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.entity.UserAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAllergyRepository extends JpaRepository<UserAllergy, Long> {
    List<UserAllergy> findByUser(User user);
    void deleteByUser(User user);
}
