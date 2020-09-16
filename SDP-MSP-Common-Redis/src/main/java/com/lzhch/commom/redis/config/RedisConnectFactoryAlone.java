package com.lzhch.commom.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * @packageName： com.lzhch.commom.redis.config
 * @className: RedisConnectFactoryAlone
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-09-03 10:18
 */
@Configuration
@ConditionalOnProperty(prefix = "edw.sdp.spring.redis", name = "isCluster", havingValue = "false")
public class RedisConnectFactoryAlone {

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private RedisServer redisServer;

    /**
     * 单机配置
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory =
                new JedisConnectionFactory(redisServer.jedisPoolConfig());
        jedisConFactory.setHostName(redisConfig.getHostName());
        jedisConFactory.setPort(redisConfig.getPort());
        jedisConFactory.setPassword(redisConfig.getPassword());
        jedisConFactory.setTimeout(redisConfig.getTimeout());
        return jedisConFactory;
    }

}
