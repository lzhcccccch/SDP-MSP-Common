package com.lzhch.commom.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @packageName： com.lzhch.commom.redis
 * @className: TestController
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-05-08 10:51
 */
@RestController
@RequestMapping("/test/")
public class TestController {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RedisLock redisLock;

    @GetMapping("get")
    public String get(String key) {
        String result = redisCache.get(key);
        System.out.println("get key:" + key + ",value :{}" + result);
        Long expire = redisCache.getExpire(key);
        System.out.println("key:" + key + ",expire :{}" + expire);
        boolean hasKey = redisCache.hasKey(key);
        System.out.println("key:" + key + ",hasKey :{}" + hasKey);
        Long del = redisCache.del("111", "222", "333");
        //Long del = redisCache.del("111");
        System.out.println("key:" + key + ",del :{}" + del);
        return result;
    }

    @PostMapping("set")
    public String set(String key, String value, Long timeout) {
        Boolean result;
        if (timeout!=null) {
            result = redisCache.set(key, value, timeout);
        } else {
            result = redisCache.set(key, value);
        }
        redisCache.set("111", "111");
        redisCache.set("222", "222");
        redisCache.set("333", "333");
        System.out.println("set key:" + key + ",value:" + value + ",result :{}" + result);
        return result.toString();
    }

    @PostMapping("lock")
    public String lock(String key, String value, long timeout) {
        boolean result = redisLock.lock(key, value, timeout);
        System.out.println("lock key:" + key + ",value:" + value + ",result :{}" + result);
        if (result) {
            return "lock success";
        }
        return "lock fail, has lock";
    }

    @DeleteMapping("unlock")
    public String unlock(String key) {
        boolean result = redisLock.unlock(key);
        System.out.println("unlock key:" + key + ",result :{}" + result);
        if (result) {
            return "unlock success";
        }
        return "unlock fail";
    }

}
