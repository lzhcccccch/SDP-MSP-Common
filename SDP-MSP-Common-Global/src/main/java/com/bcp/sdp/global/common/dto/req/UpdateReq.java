package com.bcp.sdp.global.common.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * @packageName： com.bcp.sdp.global.common.dto
 * @className: UpdateReq
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-21 09:38
 */
@Data
public class UpdateReq<ID> extends ReqDto<ID> {

    private Date updateTime;

}
