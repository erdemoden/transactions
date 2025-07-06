package com.woh.transactions.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;


public class RedisCacheStore {

    private  RedisTemplate template;

    public RedisCacheStore(RedisTemplate template) {
        this.template = template;
    }
    public void putWithExpiration(Object key, Object value, long expiration) {
        template.opsForValue().set(key, value, expiration, TimeUnit.SECONDS);
    }

    public void put(Object key, Object value) {
        template.opsForValue().set(key, value);
    }

    public void delete(Object key) {
        template.delete(key);
    }

    public <T> T get(Object key) {
        return (T) template.opsForValue().get(key);
    }
}
