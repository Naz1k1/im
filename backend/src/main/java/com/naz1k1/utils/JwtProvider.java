package com.naz1k1.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenKeyPrefix}")
    private String tokenPrefix;

    /**
     * 生成token方法
     * @param id 用户ID
     * @return token
     */
    public String generateToken(Long id) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return JWT.create()
                .withSubject(id.toString())
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC512(secret));
    }

    public Long getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = verifyToken(token);
            return Long.parseLong(jwt.getSubject());
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid token");
        }
    }

    public DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC512(secret))
                .build().verify(token);
    }

}
