package com.naz1k1.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    /**
     * 生成随机盐值
     * @return salt
     */
    public String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(16);
    }

    /**
     * 密码加密
     * @param rawPassword 原始密码
     * @param salt 盐值
     * @return 加密后的密码
     */
    public String encode(String rawPassword, String salt) {
        return DigestUtils.sha256Hex(rawPassword + salt);
    }

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param salt 盐值
     * @param encodedPassword 加密后的密码
     * @return 是否相同
     */
    public boolean matches(String rawPassword, String salt, String encodedPassword) {
        return encode(rawPassword, salt).equals(encodedPassword);
    }
}
