package com.naz1k1.service;

public interface TokenService {
    /**
     * 存储token
     * @param userId 用户ID
     * @param token token字符串
     * @param expirationTime 过期时间（毫秒）
     */
    void storeToken(Long userId, String token, long expirationTime);

    /**
     * 获取token
     * @param userId 用户ID
     * @return token字符串，如果不存在返回null
     */
    String getToken(Long userId);

    /**
     * 使token失效
     * @param userId 用户ID
     */
    void invalidateToken(Long userId);

    /**
     * 检查token是否存在且有效
     * @param userId 用户ID
     * @param token token字符串
     * @return 是否有效
     */
    boolean isTokenValid(Long userId, String token);
}
