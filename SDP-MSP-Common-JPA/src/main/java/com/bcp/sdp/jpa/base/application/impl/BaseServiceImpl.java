package com.bcp.sdp.jpa.base.application.impl;

import com.bcp.sdp.global.common.dto.req.QueryReq;
import com.bcp.sdp.global.common.dto.req.UpdateReq;
import com.bcp.sdp.global.common.dto.res.PageResult;
import com.bcp.sdp.global.common.dto.res.ResResult;
import com.bcp.sdp.global.common.utils.BeanUtil;
import com.bcp.sdp.jpa.base.application.BaseService;
import com.bcp.sdp.jpa.base.domain.BaseEntity;
import com.bcp.sdp.jpa.base.domain.PageQuery;
import com.bcp.sdp.jpa.base.persistence.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @packageName： com.bcp.sdp.jpa.base.application.impl
 * @className: BaseServiceImpl
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-13 15:23
 */
public class BaseServiceImpl<Res, ID, E extends BaseEntity<ID>, R extends BaseRepository<E, ID>> implements BaseService<Res, ID> {

    private Class<E> entityClass;
    private Class<Res> resClass;

    @Autowired
    private R repository;

    protected BaseServiceImpl() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[2];
        resClass = (Class) params[0];
    }

    @Override
    public ResResult<Res> update(UpdateReq t) {
        E entity = BeanUtil.copyProperties(t, entityClass);
        E result = repository.update(entity);
        Res res = BeanUtil.copyProperties(result, resClass);
        return ResResult.setSuccessResult(res);
    }

    @Override
    public ResResult<PageResult<Res>> getPageByAndExam(QueryReq queryReq) {
        PageQuery pageQuery = new PageQuery(queryReq.getRows(), queryReq.getPage());
        //E entity = BeanUtil.copyProperties(queryReq, entityClass); // 避免重复代码提示
        Page<E> page = repository.getPageByAndExam(BeanUtil.copyProperties(queryReq, entityClass), pageQuery);
        PageResult<Res> result = PageConvert(page);
        return ResResult.setSuccessResult(result);
    }

    @Override
    public ResResult<PageResult<Res>> getPageByAndSpec(QueryReq queryReq) {
        PageQuery pageQuery = new PageQuery(queryReq.getRows(), queryReq.getPage());
        //E entity = BeanUtil.copyProperties(queryReq, entityClass);
        Page<E> page = repository.getPageByAndSpec(BeanUtil.copyProperties(queryReq, entityClass), pageQuery);
        PageResult<Res> result = PageConvert(page);
        return ResResult.setSuccessResult(result);
    }

    @Override
    public ResResult<PageResult<Res>> getPageByOrExam(QueryReq queryReq) {
        PageQuery pageQuery = new PageQuery(queryReq.getRows(), queryReq.getPage());
        //E entity = BeanUtil.copyProperties(queryReq, entityClass);
        //Page<E> page = repository.getPageByOrExam(entity, pageQuery);
        Page<E> page = repository.getPageByOrExam(BeanUtil.copyProperties(queryReq, entityClass), pageQuery);
        PageResult<Res> result = PageConvert(page);
        return ResResult.setSuccessResult(result);
    }

    @Override
    public ResResult<PageResult<Res>> getPageByOrSpec(QueryReq queryReq) {
        PageQuery pageQuery = new PageQuery(queryReq.getRows(), queryReq.getPage());
        //E entity = BeanUtil.copyProperties(queryReq, entityClass);
        //Page<E> page = repository.getPageByOrSpec(entity, pageQuery);
        Page<E> page = repository.getPageByOrSpec(BeanUtil.copyProperties(queryReq, entityClass), pageQuery);
        PageResult<Res> result = PageConvert(page);
        return ResResult.setSuccessResult(result);
    }

    @Override
    public ResResult<PageResult<Res>> getPageByFieldsLike(QueryReq queryReq, String... field) {
        PageQuery pageQuery = new PageQuery(queryReq.getRows(), queryReq.getPage());
        E entity = BeanUtil.copyProperties(queryReq, entityClass);
        Page<E> page = repository.getPageByFieldsLike(entity, pageQuery, field);
        PageResult<Res> result = PageConvert(page);
        return ResResult.setSuccessResult(result);
    }

    //public PageResult<Res> getResult(QueryReq queryReq) {
    //    PageQuery pageQuery = new PageQuery(queryReq.getRows(), queryReq.getPage());
    //    E entity = BeanUtil.copyProperties(queryReq, entityClass);
    //    Page<E> page = repository.getPageByAndExam(entity, pageQuery);
    //    PageResult<Res> result = PageConvert(page, resClass);
    //    return result;
    //}

    public PageResult<Res> PageConvert(Page page) {
        PageResult<Res> pageResult = new PageResult<>();
        pageResult.setPage(page.getNumber()+1);
        pageResult.setRows(page.getNumberOfElements());
        pageResult.setTotalPages(page.getTotalPages());
        pageResult.setTotalRows(page.getTotalElements());
        List<Res> content = BeanUtil.copyList(page.getContent(), resClass);
        pageResult.setContent(content);
        return pageResult;
    }

}
