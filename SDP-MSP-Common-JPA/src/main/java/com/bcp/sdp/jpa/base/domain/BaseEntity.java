package com.bcp.sdp.jpa.base.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * @packageName： com.bcp.sdp.jpa.base.domain
 * @className: BaseEntity
 * @description: 实体的父类, 存放公共属性
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-13 15:36
 */
@Data
@DynamicInsert
@DynamicUpdate
@MappedSuperclass
public class BaseEntity<ID> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @Column(name = "is_delete", length = 1)
    private Integer isDelete;

    @CreationTimestamp // 该注解可单独使用, 无需其他配置
    @Column(name = "create_time", updatable = false)
    //@CreatedDate // 可注解不可单独, 需要在启动类加 @EnableJpaAuditing 注解, 实体类上加 @EntityListeners(AuditingEntityListener.class)
    private Date createTime;

    @UpdateTimestamp // 该注解可单独使用, 无需其他配置
    @Column(name = "update_time")
    //@LastModifiedDate // 可注解不可单独, 需要在启动类加 @EnableJpaAuditing 注解, 实体类上加 @EntityListeners(AuditingEntityListener.class)
    private Date updateTime;

}

/**
 1. @MappedSuperclass 注解使用在父类上面, 是用来标识父类的;
 2. @MappedSuperclass 标识的类表示其不能映射到数据库表, 因为其不是一个完整的实体类; 但是它所拥有的属性能够在其子类对应的数据库表中;
 3. @MappedSuperclass 标识的父类不能再有 @Entity 或 @Table 注解, 但其子类可以有 @Entity 或 @Table 注解, 来映射到数据库中的表
    简言之 就是告诉 jpa 不要在数据库中生成表 不要让 spring 生成 bean
 */