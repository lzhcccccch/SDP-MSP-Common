package com.lzhch.commom.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @packageName： com.lzhch.commom.redis.config
 * @className: RedisServer
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-09-03 10:21
 */
@Configuration
@ConditionalOnProperty(prefix = "edw.sdp.spring.redis", name = "isServer", havingValue = "true")
public class RedisServer {

    @Autowired
    private RedisConfig redisConfig;

    /**
     * 配置连接池
     *
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisConfig.getMaxActive());
        jedisPoolConfig.setMaxIdle(redisConfig.getMaxIdle());
        jedisPoolConfig.setMinIdle(redisConfig.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
        jedisPoolConfig.setTestOnBorrow(redisConfig.getTestOnBorrow());
        jedisPoolConfig.setMinEvictableIdleTimeMillis(redisConfig.getMinEvictableIdleTimeMillis());
        jedisPoolConfig.setTestWhileIdle(redisConfig.getTestWhileIdle());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisConfig.getTimeBetweenEvictionRunsMillis());
        jedisPoolConfig.setNumTestsPerEvictionRun(redisConfig.getNumTestsPerEvictionRun());
        return jedisPoolConfig;
    }

    /**
     * StringRedisTemplate
     * @param jedisConnectionFactory
     * @return
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        return new StringRedisTemplate(jedisConnectionFactory);
    }

}
