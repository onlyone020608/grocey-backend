package com.hyewon.grocey_api.domain.auth.service;

import com.hyewon.grocey_api.domain.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void storeRefreshToken(Long userId, String refreshToken, long ttlSeconds) {
        refreshTokenRepository.save(userId, refreshToken, ttlSeconds);
    }

    public boolean validateRefreshToken(Long userId, String refreshToken) {
        String saved = refreshTokenRepository.findByUserId(userId);
        return refreshToken.equals(saved);
    }

    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.delete(userId);
    }
}
