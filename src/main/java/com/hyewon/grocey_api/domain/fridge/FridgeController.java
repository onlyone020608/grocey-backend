package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponseDto;
import com.hyewon.grocey_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fridge")
public class FridgeController {
    private final FridgeService fridgeService;

    @GetMapping
    public ResponseEntity<FridgeResponseDto> getFridge(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(fridgeService.getFridgeInfo(userDetails.getId()));

    }
}
