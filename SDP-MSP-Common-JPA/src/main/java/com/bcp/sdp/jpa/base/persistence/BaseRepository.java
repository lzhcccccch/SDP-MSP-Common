package com.bcp.sdp.jpa.base.persistence;

import com.bcp.sdp.jpa.base.domain.PageQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @packageName： com.bcp.sdp.jpa.base.persistence
 * @className: BaseRepository
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-13 15:26
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * @description:  更新操作 为 null 的字段不进行 SQL 拼接
     * @param: [t]
     * @return: T
     * @author: liuzhichao 2021-01-26 16:25
     */
    public T update(T t);

    /**
     * @description:  重复性校验
     * @param: [obj]
     * @return: void
     * @author: liuzhichao 2021-01-26 16:25
     */
    public void isRepeat(Object obj);

    /**
     * @description:  分页查询 Example --通过 and 进行条件拼接(t 中的非空字段)
     * @param: [t, pageQuery]
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:25
     */
    public Page<T> getPageByAndExam(T t, PageQuery pageQuery);

    /**
     * @description:  分页查询 Specification --通过 and 进行条件拼接(t 中的非空字段)
     * @param: [t, pageQuery]
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:26
     */
    public Page<T> getPageByAndSpec(T t, PageQuery pageQuery);

    /**
     * @description:  分页查询 Example --通过 or 进行条件拼接(t 中非空字段)
     * @param: [t, pageQuery]
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:26
     */
    public Page<T> getPageByOrExam(T t, PageQuery pageQuery);

    /**
     * @description:  分页查询 Specification --通过 or 进行条件拼接(t 中非空字段)
     * @param: [t, pageQuery]
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:26
     */
    public Page<T> getPageByOrSpec(T t, PageQuery pageQuery);

    /**
     * @description: 分页查询--根据 queryReq 中的非空字段进行 and 连接查询, 但是 field 指定的字段进行模糊查询(%xxx%且不区分大小写)
     *  where (lower(field[0]) like ? escape ?) and (lower(field[1]) like ? escape ?) and user0_.address=? limit ?
     * @param t 入参实体
     * @param pageQuery 分页对象
     * @param field 模糊查询的字段, 必须是 t 中的
     * @return: org.springframework.data.domain.Page<T>
     * @author: liuzhichao 2021-01-26 16:26
     */
    public Page<T> getPageByFieldsLike(T t, PageQuery pageQuery, String... field);

}
/**
 *  @NoRepositoryBean
 *   1. 在服务器启动的时候, jpa 的启动管理类会自动扫荡继承了 JpaRepository 的接口, 然后添加到动态代理管理中, 然后注入到 spring 的容器中;
 *   2. 确保添加了该注解的 repository 接口不会在运行时被创建实例, 也就是说, 使用了该注解的接口不会被单独创建实例, 只会作为其他接口的父接口而被使用
 *   3. 简言之, spring 在初始化 bean 时只能初始化类对象不能初始化接口, spring 初始化相当于 new 一个对象 如果将该接口交给 spring 管理可能会出错
 *    所以不能将该接口交给 spring 应该交给 jpa 让 jpa 去解决注入问题
 *
 *    spring 注入只会生成一个 bean 开辟一块空间, 所以定义在 bean 中的全局对象是所有方法(或者请求)共享, 如果有修改会造成数据冲突
 */
