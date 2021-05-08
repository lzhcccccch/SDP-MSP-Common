package com.lzhch.commom.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @packageName： com.lzhch.commom.redis
 * @className: RedisLock
 * @description: 基于 StringRedisTemplate 实现 redis 分布式锁
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-11-16 15:34
 */
@Component
public class RedisLock {

    /**
     *  redis 锁前缀
     */
    private static String LOCK_PREFIX = "redisLock_";

    /**
     *  默认超时时间 10s
     */
    private static Long TIME_OUT = 10000L;

    @Autowired
    private StringRedisTemplate stringredisTemplate;

    /**
     * @description: redis 锁
     * @param: [key]
     * @return: boolean
     * @author: liuzhichao 2021-05-07 17:21
     */
    public boolean lock(String key) {
        return this.lock(key, LOCK_PREFIX);
    }

    /**
     * @description: redis 锁
     * @param: [key: key, value: 可以传一组 uuid 用来标识唯一请求]
     * @return: boolean
     * @author: liuzhichao 2021-05-07 17:22
     */
    public boolean lock(String key, String value) {
        return this.lock(key, value, TIME_OUT);
    }

    /**
     * @description: redis 锁
     * @param: [key: key, value: 可以传一组 uuid 用来标识唯一请求, timeout: 超时时间,单位毫秒]
     * @return: boolean
     * @author: liuzhichao 2021-05-07 17:23
     */
    public boolean lock(String key, String value, long timeout) {
        return this.lock(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * @description: redis 锁
     * @param: [key: key, value: 可以传一组 uuid 用来标识唯一请求, timeout: 超时时间,单位毫秒, timeUnit: 时间单位]
     * @return: boolean
     * @author: liuzhichao 2021-05-07 17:23
     */
    public boolean lock(String key, String value, long timeout, TimeUnit timeUnit) {
        key = LOCK_PREFIX + key;
        // setIfPresent() 方法是存在 key 时,修改 key 的值
        //Boolean success = stringredisTemplate.opsForValue().setIfPresent(key, value, timeout, timeUnit);
        Boolean success = stringredisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
        return success!=null && success;
    }

    /**
     * @description: 释放 redis 锁
     * @param: [key] key
     * @return: boolean
     * @author: liuzhichao 2021-05-07 17:33
     */
    public boolean unlock(String key) {
        key = LOCK_PREFIX + key;
        Boolean success = stringredisTemplate.delete(key);
        return success!=null && success;
    }

}
