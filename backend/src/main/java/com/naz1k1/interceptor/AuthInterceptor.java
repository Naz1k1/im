package com.naz1k1.interceptor;

import com.naz1k1.exception.UnauthorizedException;
import com.naz1k1.model.enums.RCode;
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

    public AuthInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
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
//        String token = extractToken(request);
//        if (StringUtils.isBlank(token)) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            throw new UnauthorizedException(RCode.UNAUTHORIZED.getMessage());
//        }
//        Long id = jwtProvider.getUserIdFromToken(token);
//        if (id == null) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            //todo
//            throw new UnauthorizedException(RCode.UNAUTHORIZED.getMessage());
//        }
//        request.setAttribute("userId", id);
//        return true;
        try {
            String token = extractToken(request);
            if (StringUtils.isBlank(token)) {
                return false;
            }

            Long id = jwtProvider.getUserIdFromToken(token);
            if (id == null) {
                return false;
            }

            request.setAttribute("userId", id);
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
