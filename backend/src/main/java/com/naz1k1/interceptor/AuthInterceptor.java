package com.naz1k1.interceptor;

import com.naz1k1.exception.UnauthorizedException;
import com.naz1k1.model.enums.RCode;
import com.naz1k1.service.TokenService;
import com.naz1k1.utils.JwtProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    public AuthInterceptor(JwtProvider jwtProvider,
                           TokenService tokenService) {
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
    }

    /**
     *
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return 是否拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        // 放行 OPTIONS 请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        try {
            String token = extractToken(request);
            if (StringUtils.isBlank(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new UnauthorizedException(RCode.UNAUTHORIZED.getMessage());
            }

            Long userId = jwtProvider.getUserIdFromToken(token);
            if (userId == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new UnauthorizedException(RCode.UNAUTHORIZED.getMessage());
            }
            if (!tokenService.isTokenValid(userId, token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new UnauthorizedException(RCode.UNAUTHORIZED.getMessage());
            }
            request.setAttribute("userId", userId);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return bearerToken.substring(BEARER_PREFIX.length());
    }
}
