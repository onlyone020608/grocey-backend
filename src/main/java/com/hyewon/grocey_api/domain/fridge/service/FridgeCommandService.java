package com.hyewon.grocey_api.domain.fridge.service;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FridgeCommandService {
    private final FridgeRepository fridgeRepository;

    @Transactional
    public void createFridge(Fridge fridge){
        fridgeRepository.save(fridge);
    }
}
