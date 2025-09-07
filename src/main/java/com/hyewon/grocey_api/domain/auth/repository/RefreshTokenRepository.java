package com.hyewon.grocey_api.domain.auth.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "refreshToken:";

    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(Long userId, String refreshToken, long ttlSeconds) {
        redisTemplate.opsForValue().set(PREFIX + userId, refreshToken, ttlSeconds, TimeUnit.SECONDS);
    }

    public String findByUserId(Long userId) {
        return redisTemplate.opsForValue().get(PREFIX + userId);
    }

    public void delete(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }
}
