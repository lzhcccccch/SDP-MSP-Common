package com.lzhch.common.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @packageName： com.lzhch.common.file.config
 * @className: FileConfig
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 21:52
 */
@Configuration
public class FileConfig {

    /*********************************全局配置******************************/
    /**
     * 上传本地路径
     */
    @Value("${common.file.service.upload-path}")
    public static String UPLOAD_PATH;
    /*********************************全局配置******************************/

    //@Value("${common.file.service.upload-path}")
    //public void setUploadPath(String uploadPath) {
    //    UPLOAD_PATH = uploadPath;
    //}

}
