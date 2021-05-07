package com.bcp.sdp.jpa.base.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @packageName： com.bcp.sdp.jpa.base.domain
 * @className: PageQuery
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-20 15:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery {

    private int rows;

    private int page;

    public int getPage() {
        return this.page - 1;
    }

}
