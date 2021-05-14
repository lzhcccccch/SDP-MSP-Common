package com.lzhch.commom.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @packageName： com.lzhch.commom.redis
 * @className: RedisCache
 * @description: 操作Redis缓存工具类, 封装StringRedisTemplate对象
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-09-03 10:14
 */
@Component
public class RedisCache {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    // =============================common============================

    /**
     * @description: 指定 key 失效时间
     * @param: [key, time: time>0 单位: 毫秒]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-08 16:26
     */
    public Boolean expire(String key, long time) {
        Assert.notNull(key, "redis key can not be null");
        if (time <= 0) {
            throw new IllegalArgumentException("time must be greater than zero");
        }
        return stringRedisTemplate.expire(key, time, TimeUnit.MILLISECONDS);
    }

    /**
     * @description: 查看 key 还有多久过期,单位:毫秒
     * @param: [key]
     * @return: java.lang.Long 返回 -1 表示永不过期
     * @author: liuzhichao 2021-05-08 16:45
     */
    public Long getExpire(String key) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    /**
     * @description: 判断 key 是否存在
     * @param: [key]
     * @return: java.lang.Boolean true 存在; false 不存在
     * @author: liuzhichao 2021-05-08 16:58
     */
    public Boolean hasKey(String key) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * @description: 删除 key, 可批量
     * @param: [key] 单个或多个 key
     * @return: java.lang.Long 返回的是删除的 key 的个数
     * @author: liuzhichao 2021-05-08 16:59
     */
    public Long del(String... key) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.delete(CollectionUtils.arrayToList(key));
    }

    // ============================String=============================

    /**
     * 根据 key 获取值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * @description: 存放 key 和 value 永不过期
     * @param: [key, value]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-08 17:19
     */
    public Boolean set(String key, String value) {
        Assert.notNull(key, "redis key can not be null");
        stringRedisTemplate.opsForValue().set(key, value);
        return true;
    }

    /**
     * @description: 存放 key 和 value 并设置超时时间 单位:秒
     * @param: [key, value, time: 超时时间, 单位: 秒]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-08 17:23
     */
    public Boolean set(String key, String value, long time) {
        Assert.notNull(key, "redis key can not be null");
        if (time > 0) {
            stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        }
        return this.set(key, value);
    }

    /**
     * @description: 将 key 所对应的 value 进行自增 自增因子为 1
     * @param: [key]
     * @return: java.lang.Long 返回的是自增后的 value 的值
     * @author: liuzhichao 2021-05-08 17:31
     */
    public Long incr(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    /**
     * @description: 将 key 所对应的 value 进行自增
     * @param: [key, delta: 自增因子, 必须大于 0]
     * @return: java.lang.Long 返回的是自增后的 value 的值
     * @author: liuzhichao 2021-05-08 17:31
     */
    public Long incr(String key, long delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("delta arg must be greater than zero");
        }
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * @description: 将 key 所对应的 value 进行自减, 递减因子为 1
     * @param: [key]
     * @return: java.lang.Long 返回的是自减后的 value 的值
     * @author: liuzhichao 2021-05-08 17:32
     */
    public Long decr(String key) {
        return stringRedisTemplate.opsForValue().decrement(key);
    }

    /**
     * @description: 将 key 所对应的 value 进行自减
     * @param: [key, delta: 递减因子, 必须大于 0]
     * @return: java.lang.Long 返回的是自减后的 value 的值
     * @author: liuzhichao 2021-05-08 17:32
     */
    public Long decr(String key, long delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("delta arg must be greater than zero");
        }
        return stringRedisTemplate.opsForValue().decrement(key, delta);
    }

    // ================================Map=================================

    /**
     * @description: 根据 key 和 map 集合中的 hashKey 获取 map 集合中的某个元素
     * @param: [key: redis 的 key, hashKey: map 集合中的 key]
     * @return: java.lang.Object
     * @author: liuzhichao 2021-05-08 17:51
     */
    public Object hget(String key, String hashKey) {
        Assert.notNull(key, "redis key can not be null");
        Assert.notNull(hashKey, "hashMap key can not be null");
        return stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * @description: 根据 redis key 获取 Map<String, String> 类型 value
     * @param: [key]
     * @return: java.util.Map<java.lang.String, java.lang.String>
     * @author: liuzhichao 2021-05-11 10:56
     */
    public Map<String, String> hmget(String key) {
        Assert.notNull(key, "redis key can not be null");
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
        // 将Map<Object, Object>转换为Map<String, String>
        Map<String, String> result = new HashMap<>();
        Set<Object> keys = map.keySet();
        Iterator<Object> it = keys.iterator();
        while (it.hasNext()) {
            String itemKey = (String) it.next();
            String itemValue = (String) map.get(itemKey);
            result.put(itemKey, itemValue);
        }
        return result;
    }

    /**
     * @description: 存放 map 类型的 value
     * @param: [key, map]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-11 11:04
     */
    public Boolean hmset(String key, Map<String, String> map) {
        Assert.notNull(key, "redis key can not be null");
        Assert.notNull(map, "redis value can not be null");
        try {
            stringRedisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @description: 存放 map 类型的 value 并设置超时时间
     * @param: [key, map, time]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-11 11:11
     */
    public Boolean hmset(String key, Map<String, String> map, Long time) {
        Assert.notNull(key, "redis key can not be null");
        try {
            // 标记开启事务
            stringRedisTemplate.multi();
            stringRedisTemplate.opsForHash().putAll(key, map);
            expire(key, time);
            // 执行事务块中的所有命令
            stringRedisTemplate.exec();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @description: 向 map 中添加单个元素, 已存在则修改, 否则新增
     * @param: [key, hashKey, value]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-11 11:29
     */
    public Boolean hset(String key, String hashKey, String value) {
        Assert.notNull(key, "redis key can not be null");
        Assert.notNull(hashKey, "hash key can not be null");
        try {
            stringRedisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @description: 向 map 中添加元素, 有则修改, 没有新增, 可设置超时时间
     * @param: [key, hashKey, value, time: 如果时间存在则会更新时间]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-11 11:32
     */
    public Boolean hset(String key, String hashKey, String value, Long time) {
        Assert.notNull(key, "redis key can not be null");
        Assert.notNull(hashKey, "hash key can not be null");
        try {
            stringRedisTemplate.multi();
            stringRedisTemplate.opsForHash().put(key, hashKey, value);
            expire(key, time);
            stringRedisTemplate.exec();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @description: 批量删除 map 的元素
     * @param: [key, hashKey: 不定长数组, 可多个]
     * @return: java.lang.Long   返回值为删除的元素的个数
     * @author: liuzhichao 2021-05-11 11:35
     */
    public Long hdel(String key, String... hashKey) {
        Assert.notNull(key, "redis key can not be null");
        // 将 String 转为 Object
        Object[] itemObj = hashKey;
        // 方法接收参数为 Object...
        return stringRedisTemplate.opsForHash().delete(key, itemObj);
    }

    /**
     * @description: 判断 map 中是否有某个元素
     * @param: [key, hashKey]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-11 11:36
     */
    public Boolean hHasKey(String key, String hashKey) {
        Assert.notNull(key, "redis key can not be null");
        Assert.notNull(hashKey, "hash key can not be null");
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * @description: hash 值自增, 不存在则创建
     * @param: [key, hashKey, by]
     * @return: long 自增后的值
     * @author: liuzhichao 2021-05-11 11:39
     */
    public long hincre(String key, String hashKey, long delta) {
        Assert.notNull(key, "redis key can not be null");
        Assert.notNull(hashKey, "hash key can not be null");
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * @description: hash 值递减
     * @param: [key, hashKey, delta]
     * @return: long 递减后的值
     * @author: liuzhichao 2021-05-11 17:30
     */
    public long hdecr(String key, String hashKey, long delta) {
        Assert.notNull(key, "redis key can not be null");
        Assert.notNull(hashKey, "hash key can not be null");
        return stringRedisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    // ============================set=============================

    /**
     * @description: 根据 key 获取 key 对应的set 中所有的值
     * @param: [key]
     * @return: java.util.Set<java.lang.String>
     * @author: liuzhichao 2021-05-11 17:34
     */
    public Set<String> sGet(String key) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * @description: 判断 key 对应的 set 集合中有否有 value
     * @param: [key, value: set 集合中的 value]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-11 17:35
     */
    public Boolean sHasKey(String key, String value) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * @description: 将 value 放入 key 对应的 set 集合中
     * @param: [key, values]
     * @return: java.lang.Long
     * @author: liuzhichao 2021-05-11 17:44
     */
    public Long sSet(String key, String... values) {
        Assert.notNull(key, "redis key can not be null");
        Assert.notNull(values, "set value can not be null");
        return stringRedisTemplate.opsForSet().add(key, values);
    }

    /**
     * @description: 将 value 放入 key 对应的 set 集合中
     * @param: [key, time: 超时时间, values]
     * @return: java.lang.Long
     * @author: liuzhichao 2021-05-14 14:20
     */
    public Long sSetAndTime(String key, Long time, String... values) {
        Assert.notNull(key, "redis key can not be null");
        Assert.notNull(values, "set value can not be null");
        stringRedisTemplate.multi();
        Long count = stringRedisTemplate.opsForSet().add(key, values);
        expire(key, time);
        stringRedisTemplate.exec();
        return count;
    }

    /**
     * @description: 获取 set 集合中的元素个数
     * @param: [key]
     * @return: java.lang.Long
     * @author: liuzhichao 2021-05-14 14:23
     */
    public Long sGetSetSize(String key) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * @description: 移除 set 集合中的 value 元素
     * @param: [key, values]
     * @return: java.lang.Long
     * @author: liuzhichao 2021-05-14 14:25
     */
    public Long setRemove(String key, String... values) {
        //将String转换为Object
        Object[] valuesObj = values;
        //方法接收参数为Object...
        return stringRedisTemplate.opsForSet().remove(key, valuesObj);
    }

    // ===============================list=================================

    /**
     * @description: 获取范围内 list 中的元素
     * @param: [key, start: 开始下标, end: 结束下标, -1 表示所有]
     * @return: java.util.List<java.lang.String>
     * @author: liuzhichao 2021-05-14 14:29
     */
    public List<String> lGet(String key, Long start, Long end) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    /**
     * @description: 获取 list 集合的元素的个数
     * @param: [key]
     * @return: java.lang.Long
     * @author: liuzhichao 2021-05-14 14:30
     */
    public Long lGetListSize(String key) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.opsForList().size(key);
    }

    /**
     * @description: 获取 list 结合中的某个元素
     * @param: [key, index: 索引 index>=0时, 0表头 1第二个元素, 依次类推; index<0时, -1表尾 -2倒数第二个元素, 依次类推]
     * @return: java.lang.Object
     * @author: liuzhichao 2021-05-14 14:30
     */
    public Object lGetIndex(String key, Long index) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.opsForList().index(key, index);
    }

    /**
     * @description: 将 value 放入 list, 在 list 尾进行 append 方式添加
     * @param: [key, value]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-14 15:19
     */
    public Boolean lSet(String key, String value) {
        Assert.notNull(key, "redis key can not be null");
        Long result = stringRedisTemplate.opsForList().rightPush(key, value);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * @description: 将 value 放入 list, 在 list 尾进行 append 方式添加
     * @param: [key, value]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-14 15:19
     */
    public Boolean lSet(String key, String value, Long time) {
        Assert.notNull(key, "redis key can not be null");
        stringRedisTemplate.multi();
        Long result = stringRedisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
        stringRedisTemplate.exec();
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * @description: 将 value 放入 list, 在 list 尾进行 append 方式添加
     * @param: [key, value]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-14 15:19
     */
    public Boolean lSet(String key, List<String> value) {
        Assert.notNull(key, "redis key can not be null");
        Long result = stringRedisTemplate.opsForList().rightPushAll(key, value);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * @description: 将 value 放入 list, 在 list 尾进行 append 方式添加
     * @param: [key, value]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-14 15:19
     */
    public Boolean lSet(String key, List<String> value, Long time) {
        Assert.notNull(key, "redis key can not be null");
        stringRedisTemplate.multi();
        Long result = stringRedisTemplate.opsForList().rightPushAll(key, value);
        expire(key, time);
        stringRedisTemplate.exec();
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * @description: 向某一个下标中放入元素
     * @param: [key, index, value]
     * @return: java.lang.Boolean
     * @author: liuzhichao 2021-05-14 16:11
     */
    public Boolean lUpdateIndex(String key, Long index, String value) {
        Assert.notNull(key, "redis key can not be null");
        try {
            stringRedisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @description: 删除列表中第一次出现的 value 的元素
     * @param: [key, count: >0 从左往右遍历; <0 从右往左遍历; =0 删除所有 value 元素, value]
     * @return: java.lang.Long
     * @author: liuzhichao 2021-05-14 16:25
     */
    public Long lRemove(String key, Long count, String value) {
        Assert.notNull(key, "redis key can not be null");
        return stringRedisTemplate.opsForList().remove(key, count, value);
    }

}
