package com.bcp.sdp.global.common.dto.res;

import lombok.Data;

import java.io.Serializable;

/**
 * @packageName： com.bcp.sdp.global.common.dto.res
 * @className: ResDto
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-22 14:17
 */
@Data
public class ResDto<ID> implements Serializable {

    private ID id;

}
