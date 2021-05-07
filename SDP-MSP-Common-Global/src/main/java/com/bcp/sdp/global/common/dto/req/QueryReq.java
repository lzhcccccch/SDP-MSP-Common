package com.bcp.sdp.global.common.dto.req;

import lombok.Data;

/**
 * @packageName： com.bcp.sdp.global.common.dto.req
 * @className: QueryReq
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-21 09:42
 */
@Data
public class QueryReq<ID> extends ReqDto<ID> {

    private int rows;

    private int page;

}
