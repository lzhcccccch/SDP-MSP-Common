package com.lzhch.commom.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @packageName： com.lzhch.commom.redis
 * @className: RedisLock
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-09-03 10:41
 */
@Component
@ConditionalOnProperty(prefix = "edw.sdp.spring.redis", name = "isLock", havingValue = "true")
public class RedisLock {

    @Autowired
    private StringRedisTemplate stringredisTemplate;

    /**
     * 锁过期时间,精确到毫秒
     */
    @Value("${edw.sdp.spring.redis.timeoutMsecs}")
    private String timeoutMsecs;
    /**
     * 锁后缀
     */
    @Value("${edw.sdp.spring.redis.lockSuf}")
    private String lockSuf;

    /**
     * 加锁,重载,不设置过时时间则采用默认值10*1000ms;
     * 已过时
     * @param lockKey
     * @return
     */
    public synchronized String lock(String lockKey) {
        String timeoutMsecs = String.valueOf(Long.parseLong(this.timeoutMsecs)+System.currentTimeMillis());
        if( this.lock(lockKey, timeoutMsecs) ) {
            return timeoutMsecs;
        }
        return null;
    }

    public synchronized boolean lock(String lockKey, String timeoutMsecs) {
        //超时时间早于当前时间,不能加锁
        if ( Long.parseLong(timeoutMsecs) < System.currentTimeMillis() ) {
            return false;
        }
        lockKey = lockKey + lockSuf;
        String timeout = timeoutMsecs;
        if (stringredisTemplate.opsForValue().setIfAbsent(lockKey, timeout)) {
            return true;
        }
        String currentValue = (String) stringredisTemplate.opsForValue().get(lockKey);
        if ( !StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis() ) {
            String oldValue = (String) stringredisTemplate.opsForValue().getAndSet(lockKey, timeout);
            if (oldValue != null && oldValue.equals(currentValue)) {
                return true;
            }
        }
        try {
            //采用随机值(0,1)*100ms
            Thread.sleep((int)Math.random()*100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解锁 已过时
     */
    @Deprecated
    public synchronized void unlock(String lockKey) {
        lockKey = lockKey + lockSuf;
        stringredisTemplate.opsForValue().getOperations().delete(lockKey);
    }

    /**
     *
     * @param lockKey
     * @param timeoutMsecs 超时时间与加锁时的值一样
     */
    public synchronized void unlock(String lockKey, String timeoutMsecs) {
        lockKey = lockKey + lockSuf;
        String currentValue = (String) stringredisTemplate.opsForValue().get(lockKey);
        if ( !StringUtils.isEmpty(currentValue) && currentValue.equals(timeoutMsecs) ) {
            stringredisTemplate.opsForValue().getOperations().delete(lockKey);
        }
    }

}
