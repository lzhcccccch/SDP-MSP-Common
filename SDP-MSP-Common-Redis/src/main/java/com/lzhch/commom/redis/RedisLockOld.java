package com.lzhch.commom.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @packageName： com.lzhch.commom.redis
 * @className: RedisLock
 * @description: 该加锁方式有缺陷, 由于版本低造成, 新版本 setIfAbsent() 方法已可以设置超时时间
 *  具体问题参考 'https://www.cnblogs.com/love-cj/p/8242439.html' 错误示例 2
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-09-03 10:41
 */
@Component
public class RedisLockOld {

    @Autowired
    private StringRedisTemplate stringredisTemplate;

    /**
     * 锁过期时间,精确到毫秒
     */
    private String timeoutMsecs = "10000";
    /**
     * 锁后缀
     */
    private String lockSuf = "lock_old_";

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

    /**
     * 获得 lock.
     * 实现思路: 主要是使用了redis 的setnx命令,缓存了锁.
     * reids缓存的key是锁的key,所有的共享, value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间)
     * 执行过程:
     * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
     * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
     */
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
        String currentValue = stringredisTemplate.opsForValue().get(lockKey);
        if ( !StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis() ) {
            //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
            // lock is expired
            String oldValue = stringredisTemplate.opsForValue().getAndSet(lockKey, timeout);
            //获取上一个锁到期时间，并设置现在的锁到期时间，
            //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
            if (oldValue != null && oldValue.equals(currentValue)) {
                //防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为什么相差了很少的时间，所以可以接受
                //[分布式的情况下]:如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                // lock acquired
                return true;
            }
        }
        /*
          延迟一定时间, 可以防止饥饿进程的出现, 即当同时到达多个进程
          只会有一个进程获得锁 其他的都用同样的频率进行尝试 后面有来了一些进行 也以同样的频率申请锁 这将可能导致前面来的锁得不到满足.
          使用随机的等待时间可以一定程度上保证公平性
        */
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
