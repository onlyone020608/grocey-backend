package com.hyewon.grocey_api.auth;

import com.hyewon.grocey_api.auth.dto.LoginRequest;
import com.hyewon.grocey_api.auth.dto.TokenRefreshRequest;
import com.hyewon.grocey_api.auth.dto.TokenResponse;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final Map<Long, String> refreshTokenStore = new HashMap<>(); // 실무에선 DB로 대체

    public AuthController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmailAndPassword(
                        request.getEmail(), request.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        refreshTokenStore.put(user.getId(), refreshToken); // 추후 DB 저장으로 대체

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        String savedRefreshToken = refreshTokenStore.get(userId);
        if (!refreshToken.equals(savedRefreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
    }
}
