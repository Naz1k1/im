package com.naz1k1.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider {
    private final RedisTemplate<Object, Object> redisTemplate;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    public JwtTokenProvider(RedisTemplate<Object, Object> redisTemplate) {
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
        redisTemplate.opsForValue().set(
                "token:"+userId,token,24, TimeUnit.HOURS
        );
        return token;
    }

    /**
     * 验证并解析Token
     */
    public Long getUserIdFormToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token);
            return Long.parseLong(jwt.getSubject());
        }catch (JWTVerificationException exception){
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token);

            long userId = Long.parseLong(jwt.getSubject());

            String storedToken = (String) redisTemplate.opsForValue().get("token:"+userId);
            return token.equals(storedToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
