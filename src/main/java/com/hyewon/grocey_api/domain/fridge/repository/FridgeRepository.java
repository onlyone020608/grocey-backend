package com.hyewon.grocey_api.domain.fridge.repository;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeRepository extends JpaRepository<Fridge, Long> {
}
