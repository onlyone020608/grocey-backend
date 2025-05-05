package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fridge")
public class FridgeController {
    private final FridgeService fridgeService;

    // TODO: Replace with authenticated user (use /me endpoint after JWT setup)
    @GetMapping("/{userId}")
    public ResponseEntity<FridgeResponseDto> getFridge(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(fridgeService.getFridgeInfo(userId));

    }
}
