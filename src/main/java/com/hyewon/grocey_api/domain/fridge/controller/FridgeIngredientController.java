package com.hyewon.grocey_api.domain.fridge.controller;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientDetailResponse;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponse;
import com.hyewon.grocey_api.domain.auth.security.CustomUserDetails;
import com.hyewon.grocey_api.domain.fridge.service.FridgeIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fridge/ingredients")
@RequiredArgsConstructor
public class FridgeIngredientController {
    private final FridgeIngredientService fridgeIngredientService;

    @GetMapping
    public List<FridgeIngredientResponse> getFridgeIngredients(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @RequestParam(required = false) boolean isFreezer) {
        return fridgeIngredientService.getIngredientsByFridge(userDetails.getUser().getFridge().getId(), isFreezer);
    }

    @GetMapping("/{ingredientId}")
    public FridgeIngredientDetailResponse getIngredientDetail(@PathVariable Long ingredientId) {
        return fridgeIngredientService.getIngredientDetail(ingredientId);
    }
}
