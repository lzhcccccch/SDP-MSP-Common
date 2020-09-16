package com.lzhch.file.oss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @packageName： com.lzhch.file.oss.config
 * @className: OssConfig
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-09-01 10:54
 */
@Configuration
public class OssConfig {

    /*********************************阿里云（OSS）******************************/
    /**
     * 端地址
     */
    public static String ENDPOINT;

    /**
     * 接入键ID
     */
    public static String ACCESS_KEYID;

    /**
     * 接入密钥
     */
    public static String ACCESS_KEYSECRET;

    /**
     * 空间名
     */
    public static String BUCKET_NAME;
    /*********************************阿里云（OSS）******************************/

    @Value("${common.file.oss.endpoint}")
    public void setENDPOINT(String ENDPOINT) {
        OssConfig.ENDPOINT = ENDPOINT;
    }

    @Value("${common.file.oss.access-keyid}")
    public void setAccessKeyid(String accessKeyid) {
        ACCESS_KEYID = accessKeyid;
    }

    @Value("${common.file.oss.access-keysecret}")
    public void setAccessKeysecret(String accessKeysecret) {
        ACCESS_KEYSECRET = accessKeysecret;
    }

    @Value("${common.file.oss.bucket-name}")
    public void setBucketName(String bucketName) {
        BUCKET_NAME = bucketName;
    }

}
