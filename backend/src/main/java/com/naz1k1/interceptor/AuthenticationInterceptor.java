package com.naz1k1.interceptor;

import com.naz1k1.entity.User;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserMapper userMapper;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public AuthenticationInterceptor(UserMapper userMapper, RedisTemplate<Object, Object> redisTemplate, JwtTokenProvider jwtTokenProvider) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 获取请求头中的token
        String token = extractToken(request);
        if (StringUtils.isBlank(token)) {
            log.warn("请求未携带token, URI: {}", request.getRequestURI());
            throw new RuntimeException("未提供认证令牌");
        }

        // 从token中获取用户ID
        Long userId = jwtTokenProvider.getUserIdFormToken(token);
        if (userId == null) {
            log.warn("无效的token: {}", token);
            throw new RuntimeException("无效的认证令牌");
        }

        // 检查Redis中的token是否存在且匹配
        String storedToken = (String) redisTemplate.opsForValue().get("token:" + userId);
        if (storedToken == null || !storedToken.equals(token)) {
            throw new RuntimeException("认证令牌已失效，请重新登录");
        }

        // 验证用户是否存在
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        log.info("用户认证成功 - userId: {}, URI: {}", userId, request.getRequestURI());
        // 将用户信息存储在请求属性中
        request.setAttribute("currentUser", user);
        request.setAttribute("userId", userId);

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
