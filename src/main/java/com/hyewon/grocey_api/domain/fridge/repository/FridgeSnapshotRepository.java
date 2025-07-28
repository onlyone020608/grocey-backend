package com.hyewon.grocey_api.domain.fridge.repository;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeSnapshotRepository extends JpaRepository<FridgeSnapshot,Long> {
}
