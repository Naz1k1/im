package com.naz1k1.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    /**
     * 设置缓存
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取缓存
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 删除缓存
     */
    void delete(String key);

    /**
     * 批量删除缓存
     */
    void delete(List<String> keys);
}
