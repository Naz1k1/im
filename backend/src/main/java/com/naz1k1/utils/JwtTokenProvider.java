package com.naz1k1.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成Token
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC512(secret));
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
}
