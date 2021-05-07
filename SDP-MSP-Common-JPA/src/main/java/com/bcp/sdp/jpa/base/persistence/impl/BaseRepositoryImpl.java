package com.bcp.sdp.jpa.base.persistence.impl;

import com.bcp.sdp.global.common.utils.BeanUtil;
import com.bcp.sdp.jpa.base.domain.BaseEntity;
import com.bcp.sdp.jpa.base.domain.PageQuery;
import com.bcp.sdp.jpa.base.persistence.BaseRepository;
import com.bcp.sdp.jpa.common.annotations.Repeat;
import com.bcp.sdp.jpa.common.core.SpecificationOption;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @packageName： com.bcp.sdp.jpa.base.persistence.impl
 * @className: BaseRepositoryImpl
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-13 15:27
 */
public class BaseRepositoryImpl<T extends BaseEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public BaseRepositoryImpl(Class domainClass, EntityManager em) {
        super(domainClass, em);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T update(T t) {
        Assert.notNull(t, "传入对象不能为 null");
        ID id = (ID) t.getId();
        Assert.notNull(id, "传入对象的 id 不能为 null");
        isRepeat(t); // 重复性校验
        T entity = findById(id).get();
        BeanUtil.copyNotNullProperties(t, entity);
        return save(entity);
    }

    @Override
    public void isRepeat(Object obj) {
        Assert.notNull(obj, "传入对象不能为 null");
        // 获取所有字段
        List<Field> fields = SpecificationOption.getAllFieldsWithRoot(obj.getClass());
        Assert.notNull(fields, "对象中 fields 不能为 null");
        // 遍历字段获取所需字段
        for (Field item : fields) {
            Object value = null;
            try {
                // 类外访问 private 属性必须添加
                item.setAccessible(true);
                // 获取注解
                Repeat repeat = item.getAnnotation(Repeat.class);
                // 校验字段
                if (repeat != null) {
                    // 获取加注解属性对应的值
                    value = item.get(obj);
                    if (value != null && !"".equals(value)) {
                        String name = item.getName();
                        Specification spec = SpecificationOption.isRepeatByField(name, value);
                        List list = findAll(spec);
                        if (list != null && list.size() > 0) {
                            throw new RuntimeException(repeat.message());
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Page<T> getPageByAndExam(T t, PageQuery pageQuery) {
        Example<T> example = Example.of(t);
        Page<T> result = findAll(example, PageRequest.of(pageQuery.getPage(), pageQuery.getRows()));
        return result;
    }

    @Override
    public Page<T> getPageByAndSpec(T t, PageQuery pageQuery) {
        Specification<T> spec = (root, cq, cb) -> {
            // Example 默认采用 ExampleMatcher.matching() 而 ExampleMatcher.matching() 调用 ExampleMatcher.matchingAll(), 所以用 and 进行条件拼接
            Example<T> example = Example.of(t);
            return QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
        };
        Page<T> result = findAll(spec, PageRequest.of(pageQuery.getPage(), pageQuery.getRows()));
        return result;
    }

    @Override
    public Page<T> getPageByOrExam(T t, PageQuery pageQuery) {
        ExampleMatcher matcher = ExampleMatcher.matchingAny();
        Example<T> example = Example.of(t, matcher);
        Page<T> result = findAll(example, PageRequest.of(pageQuery.getPage(), pageQuery.getRows()));
        return result;
    }

    @Override
    public Page<T> getPageByOrSpec(T t, PageQuery pageQuery) {
        Specification<T> spec = (root, cq, cb) -> {
            // ExampleMatcher.matchingAny() 表示 or 连接查询
            ExampleMatcher matcher = ExampleMatcher.matchingAny();
            Example<T> example = Example.of(t, matcher);
            return QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
        };
        Page<T> result = findAll(spec, PageRequest.of(pageQuery.getPage(), pageQuery.getRows()));
        return result;
    }

    @Override
    public Page<T> getPageByFieldsLike(T t, PageQuery pageQuery, String... field) {
        if (field==null || field.length<1) {
            return getPageByAndExam(t, pageQuery);
        }
        ExampleMatcher matcher = ExampleMatcher.matchingAll();
        for (String item : field) {
            //matcher = matcher.withMatcher(item, match -> match.contains()); // lamda 表达式
            matcher = matcher.withMatcher(item, ExampleMatcher.GenericPropertyMatchers.contains()); // 模糊查询 %xxx%
        }
        // matcher.withIgnoreCase() 忽略大小写 默认为 true 单条件直接加在 matcher.withMatcher() 即可
        // 多条件时可以指定对哪个字段忽略大小写 如下 filed 是数组, 也可 matcher.withIgnoreCase(field1, field2, "xxx")
        matcher = matcher.withIgnoreCase(field);
        Example<T> example = Example.of(t, matcher);
        Page<T> result = findAll(example, PageRequest.of(pageQuery.getPage(), pageQuery.getRows()));
        return result;
    }

    /**
     *  设置排序
     */
    public void setSort() {
        // JPA 提供的排序的方法 Sort.by();
        //public static Sort by(String... properties); // 可以传多个属性, 所传属性均采用默认的排序方式
        //public static Sort by(List<Sort.Order> orders); // 传 Order 的 list 对象, 可以对每个属性设置排序方式
        //public static Sort by(Sort.Order... orders); // 可以传多个 Order 对象, 可以对每个属性设置排序方式
        //public static Sort by(Sort.Direction direction, String... properties) // 可以传多个属性, 所传属性均采用相同的排序方式
        // 1
        Sort sort1 = Sort.by("xxx", "yyy"); // 对字段 xxx 和 yyy 均采用默认的排序方式
        // 2
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "xxx"); // 对 xxx 采用升序
        Sort.Order order2 = Sort.Order.desc("yyy"); // 对 yyy 采用降序
        Sort sort2 = Sort.by(Arrays.asList(order1, order2)); // 对 xxx 采用升序并且对 yyy 采用降序
        // 3 (和 2 基本类似)
        Sort.Order order3 = new Sort.Order(Sort.Direction.DESC, "xxx"); // 对 xxx 采用降序
        Sort.Order order4 = Sort.Order.asc("yyy"); // 对 yyy 采用升序
        Sort sort3 = Sort.by(order1, order2); // 对 xxx 采用降序并且对 yyy 采用升序
        // 4
        Sort sort4 = Sort.by(Sort.Direction.DESC, "xxx", "yyy"); // 对 xxx 和 yyy 均采用降序

        PageRequest.of(0, 10, sort1);
        PageRequest.of(0, 10, Sort.Direction.DESC, "xxx", "yyy");
    }

}
