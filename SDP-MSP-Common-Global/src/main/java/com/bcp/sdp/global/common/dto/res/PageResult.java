package com.bcp.sdp.global.common.dto.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @packageName： com.bcp.sdp.global.common.dto.res
 * @className: PageResult
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-21 09:49
 */
@Data
public class PageResult<R> implements Serializable {

    private int page;

    private int rows;

    private int totalPages;

    private long totalRows;

    private List<R> content;

}
