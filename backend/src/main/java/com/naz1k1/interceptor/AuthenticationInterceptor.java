package com.naz1k1.interceptor;

import com.naz1k1.mapper.UserMapper;
import com.naz1k1.utils.JwtTokenProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserMapper userMapper;
    private final RedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationInterceptor(UserMapper userMapper, RedisTemplate redisTemplate, JwtTokenProvider jwtTokenProvider) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }


}
