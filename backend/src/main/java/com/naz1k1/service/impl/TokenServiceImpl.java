package com.naz1k1.service.impl;

import com.naz1k1.service.RedisService;
import com.naz1k1.service.TokenService;
import com.naz1k1.utils.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {
    private static final String TOKEN_KEY_PREFIX = "token:";

    private final RedisService redisService;

    public TokenServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void storeToken(Long userId, String token, long expirationTime) {
        String key = TOKEN_KEY_PREFIX + userId;
        redisService.set(key, token, expirationTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getToken(Long userId) {
        String key = TOKEN_KEY_PREFIX + userId;
        return redisService.get(key, String.class);
    }

    @Override
    public void invalidateToken(Long userId) {
        String key = TOKEN_KEY_PREFIX + userId;
        redisService.delete(key);
    }

    @Override
    public boolean isTokenValid(Long userId, String token) {
        String storedToken = getToken(userId);
        return storedToken != null && storedToken.equals(token);
    }
}

