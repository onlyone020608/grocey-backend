package com.hyewon.grocey_api.domain.fridge.service;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeSnapshot;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FridgeSnapshotCommandService {
    private final FridgeSnapshotRepository fridgeSnapshotRepository;

    @Transactional
    public void createFridgeSnapshot(FridgeSnapshot snapshot) {
        fridgeSnapshotRepository.save(snapshot);
    }
}
