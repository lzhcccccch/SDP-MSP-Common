package com.bcp.sdp.global.common.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @packageName： com.bcp.sdp.global.common.dto.req
 * @className: ReqDto
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-22 14:17
 */
@Data
public class ReqDto<ID> implements Serializable {

    private ID id;

}
