package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fridge/{fridgeId}/ingredients")
@RequiredArgsConstructor
public class FridgeIngredientController {
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final FridgeService fridgeService;

    @GetMapping
    public List<FridgeIngredientResponseDto> getFridgeIngredients(@PathVariable Long fridgeId,
                                                                  @RequestParam(required = false) boolean isFreezer) {
        return fridgeService.getIngredientsByFridge(fridgeId, isFreezer);
    }
}
