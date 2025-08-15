package com.hyewon.grocey_api.domain.user.repository;

import com.hyewon.grocey_api.domain.user.entity.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {
    boolean existsByAllergyName(String allergyName);
}
