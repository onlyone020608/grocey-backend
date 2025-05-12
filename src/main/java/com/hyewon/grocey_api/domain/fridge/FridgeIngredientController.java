package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientDetailResponseDto;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponseDto;
import com.hyewon.grocey_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fridge/{fridgeId}/ingredients")
@RequiredArgsConstructor
public class FridgeIngredientController {
    private final FridgeIngredientService fridgeIngredientService;

    @GetMapping
    public List<FridgeIngredientResponseDto> getFridgeIngredients(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @RequestParam(required = false) boolean isFreezer) {
        return fridgeIngredientService.getIngredientsByFridge(userDetails.getUser().getFridge().getId(), isFreezer);
    }

    @GetMapping("/{ingredientId}")
    public FridgeIngredientDetailResponseDto getIngredientDetail(@PathVariable Long ingredientId) {
        return fridgeIngredientService.getIngredientDetail(ingredientId);
    }
}
