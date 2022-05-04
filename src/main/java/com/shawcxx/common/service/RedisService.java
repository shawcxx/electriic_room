package com.shawcxx.common.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author cjl
 * @date 2021/11/19 13:44
 * @description
 */
@Service
public class RedisService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void putValue(String key, String value, Integer expire, TimeUnit unit) {
        if (expire == null || unit == null) {
            stringRedisTemplate.opsForValue().set(key, value);
        } else {
            stringRedisTemplate.opsForValue().set(key, value, expire, unit);
        }
    }

    public String getValue(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return value;
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }
}
