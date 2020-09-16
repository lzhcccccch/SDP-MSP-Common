package com.lzhch.common.file.dto;

import lombok.Data;

/**
 * @packageName： com.lzhch.common.file.dto
 * @className: FileRes
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 16:08
 */
@Data
public class FileRes {

    /**
     *  上传至服务器的路径名字
     */
    private String keys;

    /**
     *  原文件名字和上传至服务器后的路径
     */
    private String namesAndKeys;

}
