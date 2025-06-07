package com.naz1k1.interceptor;

import com.naz1k1.entity.User;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TOKEN_KEY_PREFIX = "im:token:";
    private static final String ONLINE_KEY_PREFIX = "im:online:";
    private static final long ONLINE_EXPIRE_TIME = 180; // 秒
    private static final String WEBSOCKET_UPGRADE = "websocket";

    public AuthenticationInterceptor(UserMapper userMapper, RedisTemplate<String, Object> redisTemplate, JwtTokenProvider jwtTokenProvider) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 处理OPTIONS请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 处理WebSocket升级请求
        if (isWebSocketUpgrade(request)) {
            return handleWebSocketAuthentication(request, response);
        }

        // 获取并验证token
        String token = extractToken(request);
        if (StringUtils.isBlank(token)) {
            log.warn("请求未携带token, URI: {}", request.getRequestURI());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new RuntimeException("未提供认证令牌");
        }

        // 验证token并获取用户ID
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        if (userId == null) {
            log.warn("无效的token: {}", token);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new RuntimeException("无效的认证令牌");
        }

        // 验证Redis中的token
        String tokenKey = TOKEN_KEY_PREFIX + userId;
        Object storedToken = redisTemplate.opsForValue().get(tokenKey);
        if (storedToken == null || !token.equals(storedToken.toString())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new RuntimeException("认证令牌已失效，请重新登录");
        }

        // 验证用户是否存在
        User user = userMapper.findById(userId);
        if (user == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new RuntimeException("用户不存在");
        }

        // 更新用户在线状态
        updateUserOnlineStatus(userId);

        // 检查是否需要刷新token
        if (jwtTokenProvider.shouldTokenBeRefreshed(token)) {
            String newToken = jwtTokenProvider.refreshToken(token);
            redisTemplate.opsForValue().set(tokenKey, newToken, ONLINE_EXPIRE_TIME, TimeUnit.SECONDS);
        }

        log.info("用户认证成功 - userId: {}, URI: {}", userId, request.getRequestURI());
        // 将用户信息存储在请求属性中
        request.setAttribute("currentUser", user);
        request.setAttribute("userId", userId);

        return true;
    }

    private boolean isWebSocketUpgrade(HttpServletRequest request) {
        return WEBSOCKET_UPGRADE.equalsIgnoreCase(request.getHeader("Upgrade"));
    }

    private boolean handleWebSocketAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        if (userId == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 验证WebSocket连接的token
        Object storedToken = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + userId);
        if (storedToken == null || !token.equals(storedToken.toString())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        request.setAttribute("userId", userId);
        return true;
    }

    private void updateUserOnlineStatus(Long userId) {
        String onlineKey = ONLINE_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(onlineKey, "online", ONLINE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken == null) {
            log.warn("Authorization 请求头为 null");
            return null;
        }
        if (!bearerToken.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return bearerToken.substring(BEARER_PREFIX.length());
    }
}
