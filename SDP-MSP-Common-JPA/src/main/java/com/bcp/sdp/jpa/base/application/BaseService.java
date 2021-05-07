package com.bcp.sdp.jpa.base.application;

import com.bcp.sdp.exception.service.MyService;
import com.bcp.sdp.global.common.dto.req.QueryReq;
import com.bcp.sdp.global.common.dto.req.UpdateReq;
import com.bcp.sdp.global.common.dto.res.PageResult;
import com.bcp.sdp.global.common.dto.res.ResResult;

/**
 * @packageName： com.bcp.sdp.jpa.base.application
 * @className: BaseService
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-1-13 15:22
 */
public interface BaseService<R, ID> extends MyService {

    /**
     * @description:  更新操作 为 null 的字段不进行 SQL 拼接
     * @param: [t]
     * @return: T
     * @author: liuzhichao 2021-01-26 16:25
     */
    public ResResult<R> update(UpdateReq<ID> updateReq);

    /**
     * @description:  分页查询 Example --通过 and 进行条件拼接(t 中的非空字段)
     * @param: [t, pageQuery]
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:25
     */
    public ResResult<PageResult<R>> getPageByAndExam(QueryReq<ID> queryReq);

    /**
     * @description:  分页查询 Specification --通过 and 进行条件拼接(t 中的非空字段)
     * @param: [t, pageQuery]
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:26
     */
    public ResResult<PageResult<R>> getPageByAndSpec(QueryReq<ID> queryReq);

    /**
     * @description:  分页查询 Example --通过 or 进行条件拼接(t 中非空字段)
     * @param: [t, pageQuery]
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:26
     */
    public ResResult<PageResult<R>> getPageByOrExam(QueryReq<ID> queryReq);

    /**
     * @description:  分页查询 Specification --通过 or 进行条件拼接(t 中非空字段)
     * @param: [t, pageQuery]
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:26
     */
    public ResResult<PageResult<R>> getPageByOrSpec(QueryReq<ID> queryReq);

    /**
     * @description: 分页查询--根据 queryReq 中的非空字段进行 and 连接查询, 但是 field 指定的字段进行模糊查询(%xxx%且不区分大小写)
     *  where (lower(field[0]) like ? escape ?) and (lower(field[1]) like ? escape ?) and user0_.address=? limit ?
     * @param queryReq  入参对象
     * @param field 模糊查询的字段, 必须是 queryReq 中且是数据库中存在的字段, 字段对标实体不对标数据库表
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:26
     */
    public ResResult<PageResult<R>> getPageByFieldsLike(QueryReq<ID> queryReq, String... field);

}
