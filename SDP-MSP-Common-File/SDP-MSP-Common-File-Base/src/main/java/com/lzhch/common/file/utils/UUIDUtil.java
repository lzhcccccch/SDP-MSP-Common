package com.lzhch.common.file.utils;

import java.util.UUID;

/**
 * @packageName： com.lzhch.common.file.utils
 * @className: UUIDUtil
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 21:58
 */
public class UUIDUtil {

    public UUIDUtil() {
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
