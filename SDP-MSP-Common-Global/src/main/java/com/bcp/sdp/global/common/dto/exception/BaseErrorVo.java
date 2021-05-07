package com.bcp.sdp.global.common.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @packageName： com.bcp.sdp.jpa.common.vo
 * @className: BaseErrorVo
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-20 22:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseErrorVo implements Serializable {

    /**
     *  错误信息编号
     */
    private Integer errorCode;

    /**
     *  错误信息描述
     */
    private String errorMessage;

}
