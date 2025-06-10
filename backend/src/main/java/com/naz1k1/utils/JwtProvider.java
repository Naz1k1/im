package com.naz1k1.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

    public String generateToken(Long id) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return JWT.create()
                .withSubject(id.toString())
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC512(secret));
    }
}
