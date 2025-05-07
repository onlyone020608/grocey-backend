package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientDetailResponseDto;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fridge/{fridgeId}/ingredients")
@RequiredArgsConstructor
public class FridgeIngredientController {
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final FridgeIngredientService fridgeIngredientService;

    @GetMapping
    public List<FridgeIngredientResponseDto> getFridgeIngredients(@PathVariable Long fridgeId,
                                                                  @RequestParam(required = false) boolean isFreezer) {
        return fridgeIngredientService.getIngredientsByFridge(fridgeId, isFreezer);
    }

    @GetMapping("/{id}")
    public FridgeIngredientDetailResponseDto getIngredientDetail(@PathVariable Long id) {
        return fridgeIngredientService.getIngredientDetail(id);
    }
}
