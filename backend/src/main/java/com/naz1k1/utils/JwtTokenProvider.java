package com.naz1k1.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtTokenProvider {
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration; // Token有效期（毫秒）

    private static final String TOKEN_KEY_PREFIX = "im:token:";
    private static final long TOKEN_MINIMUM_LIFETIME = 1800000; // 30分钟

    public JwtTokenProvider(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成Token
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        String token = JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC512(secret));

        // 存储token
        String tokenKey = TOKEN_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(tokenKey, token, expiration, TimeUnit.MILLISECONDS);

        return token;
    }

    /**
     * 验证并解析Token
     */
    public Long getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return Long.parseLong(jwt.getSubject());
        } catch (JWTVerificationException e) {
            log.warn("Token验证失败: {}", e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            log.error("Token中的用户ID格式错误: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证Token
     */
    private DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token);
    }

    /**
     * 判断Token是否需要刷新
     */
    public boolean shouldTokenBeRefreshed(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            Date expirationDate = jwt.getExpiresAt();
            return expirationDate.getTime() - System.currentTimeMillis() < TOKEN_MINIMUM_LIFETIME;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 刷新Token
     */
    public String refreshToken(String oldToken) {
        try {
            DecodedJWT jwt = verifyToken(oldToken);
            Long userId = Long.parseLong(jwt.getSubject());
            return generateToken(userId);
        } catch (JWTVerificationException e) {
            log.error("Token刷新失败: {}", e.getMessage());
            throw new RuntimeException("Token刷新失败");
        }
    }

    /**
     * 验证Token有效性
     */
    public boolean validateToken(String token) {
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                return false;
            }

            String tokenKey = TOKEN_KEY_PREFIX + userId;
            Object storedToken = redisTemplate.opsForValue().get(tokenKey);
            return token.equals(storedToken);
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }
}
