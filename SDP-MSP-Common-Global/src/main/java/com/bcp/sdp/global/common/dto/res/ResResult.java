package com.bcp.sdp.global.common.dto.res;

import com.bcp.sdp.global.common.dto.exception.BaseErrorVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @packageName： com.bcp.sdp.jpa.common.vo
 * @className: ResResult
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-20 22:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResResult<T> implements Serializable {

    private boolean success;

    private T data;

    private BaseErrorVo baseErrorVo;

    public static ResResult setSuccessResult() {
        return new ResResult(true, null, null);
    }

    public static <T> ResResult setSuccessResult(T t) {
        return new ResResult(true, t, null);
    }

    public static ResResult setErrorResult(BaseErrorVo baseErrorVo) {
        return new ResResult(false, null, baseErrorVo);
    }

}
