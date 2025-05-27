package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponseDto;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class FridgeService {
    private final FridgeRepository fridgeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public FridgeResponseDto getFridgeInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Fridge fridge = user.getFridge();
        if (fridge == null) {
            throw new FridgeNotFoundException(userId);
        }

        return new FridgeResponseDto(fridge.getFridgeTemperature(), fridge.getFreezerTemperature());
    }


}
