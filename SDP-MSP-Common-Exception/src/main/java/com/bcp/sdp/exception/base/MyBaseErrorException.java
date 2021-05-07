package com.bcp.sdp.exception.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @packageName： com.bcp.sdp.exception.base
 * @className: MyBaseErrorException
 * @description:  自己定义的异常类, 可以抛出被切面捕捉
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-26 16:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyBaseErrorException extends RuntimeException {

    /**
     *  异常信息编号
     */
    private Integer errorCode;

    /**
     *  异常信息描述
     */
    private String errorMsg;

}
