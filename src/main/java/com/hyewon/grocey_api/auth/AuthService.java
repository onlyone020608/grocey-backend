package com.hyewon.grocey_api.auth;

import com.hyewon.grocey_api.auth.dto.LoginRequest;
import com.hyewon.grocey_api.auth.dto.SignupRequest;
import com.hyewon.grocey_api.auth.dto.TokenRefreshRequest;
import com.hyewon.grocey_api.auth.dto.TokenResponse;
import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import com.hyewon.grocey_api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final FridgeRepository fridgeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Map<Long, String> refreshTokenStore = new HashMap<>();



    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }


        Fridge fridge = new Fridge(3.0, -18.0);
        fridgeRepository.save(fridge);

        User user = new User(request.getName(), request.getEmail(), request.getPassword());
        user.assignFridge(fridge); // 연관관계 설정

        userRepository.save(user);
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        refreshTokenStore.put(user.getId(), refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String saved = refreshTokenStore.get(userId);

        if (!refreshToken.equals(saved)) {
            throw new IllegalArgumentException("Refresh token mismatch");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        return new TokenResponse(newAccessToken, refreshToken);
    }

    public void logout(Long userId) {
        refreshTokenStore.remove(userId);
    }

    public void withdraw(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        refreshTokenStore.remove(userId);
        userRepository.deleteById(userId);
    }

}
