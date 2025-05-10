package com.hyewon.grocey_api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAllergyRepository extends JpaRepository<UserAllergy, Long> {
    void deleteByUser(User user);
}
