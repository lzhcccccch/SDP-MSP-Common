package com.lzhch.commom.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @packageName： com.lzhch.commom.redis
 * @className: RedisApplication
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-05-08 10:47
 */
@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
        System.out.println("--------start--------");
    }

}
