package com.karthick.Expenz.common;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheData(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
    }

    public Object retrieveDateFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteCachedData(String key) {
        redisTemplate.delete(key);
    }
}
