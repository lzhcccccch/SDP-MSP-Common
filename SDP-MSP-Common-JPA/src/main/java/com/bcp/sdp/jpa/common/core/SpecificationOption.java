package com.bcp.sdp.jpa.common.core;

import com.bcp.sdp.jpa.common.annotations.QueryCondition;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @packageName： com.bcp.sdp.jpa.common.core
 * @className: SpecificationOption
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-14 11:14
 */
public class SpecificationOption {

    /**
     *  获取类 clazz 的所有 Field 包括其父类的 Field
     * @param clazz
     * @return
     */
    public static List<Field> getAllFieldsWithRoot(Class<?> clazz) {
        if (clazz==null) {
            return null;
        }
        List<Field> fieldList = new ArrayList<>();
        // 获取当前类的所有字段
        Field[] clazzFields = clazz.getDeclaredFields();
        if (null != clazzFields && clazzFields.length > 0) {
            fieldList.addAll(Arrays.asList(clazzFields));
        }
        // 若父类是 Object 则直接返回当前 Field 列表
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == Object.class) {
            return Arrays.asList(clazzFields);
        }
        // 递归查询父类的 field 列表 将父类的字段添加到 fieldList 中
        List<Field> superFields = getAllFieldsWithRoot(superClass);
        //将父类的字段全部添加进来并进行去重
        if (null != superFields && !superFields.isEmpty()) {
            superFields.stream().
                    filter(field -> !fieldList.contains(field)).//不重复字段
                    forEach(field -> fieldList.add(field));
        }
        return fieldList;
    }

    /**
     *  判断字段是否重复的条件构造
     * @param name
     * @param value
     * @return
     */
    public static Specification isRepeatByField(String name, Object value) {
        Specification spec = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path path = root.get(name);
                return criteriaBuilder.equal(path, value);
            }
        };
        return spec;
    }

    /**
     *  查询条件拼接 非空字段全部拼接 用 and 连接
     * @param obj
     * @return
     */
    public static Specification toSpecWithLogicType(Object obj) {
        Specification specification = (root, query, cb) -> {
            //获取查询类Query的所有字段,包括父类字段
            List<Field> fields = getAllFieldsWithRoot(obj.getClass());
            List<Predicate> predicates = new ArrayList<>();
            //循环字段
            for (Field field : fields) {
                try {
                    //设置访问private修饰的属性
                    field.setAccessible(true);
                    //获取字段的值
                    Object value = field.get(obj);
                    //获取字段上的@QueryCondition注解
                    QueryCondition qw = field.getAnnotation(QueryCondition.class);
                    //字段没有加注解跳过拼接，但是字段值不为null则拼接and eq条件
                    if (qw == null) {
                        if (value != null && !value.equals("")) {
                            Path path = root.get(field.getName());
                            predicates.add(cb.equal(path, value));
                        }
                        continue;
                    }
                    //判断加了注解字段是否是空值
                    if (value == null || value.equals(""))
                        continue;
                    // 获取字段名
                    String column = qw.column();
                    //如果主注解上colume为默认值"",则以field为准
                    Path path = root.get(field.getName());
                    switch (qw.func()) {
                        case equal:
                            predicates.add(cb.equal(path, value));
                            break;
                        case like:
                            predicates.add(cb.like(path, "%" + value + "%"));
                            break;
                        case gt:
                            predicates.add(cb.gt(path, (Number) value));
                            break;
                        case lt:
                            predicates.add(cb.lt(path, (Number) value));
                            break;
                        case ge:
                            predicates.add(cb.ge(path, (Number) value));
                            break;
                        case le:
                            predicates.add(cb.le(path, (Number) value));
                            break;
                        case notEqual:
                            predicates.add(cb.notEqual(path, value));
                            break;
                        case notLike:
                            predicates.add(cb.notLike(path, "%" + value + "%"));
                            break;
                        case greaterThan:
                            predicates.add(cb.greaterThan(path, (Comparable) value));
                            break;
                        case greaterThanOrEqualTo:
                            predicates.add(cb.greaterThanOrEqualTo(path, (Comparable) value));
                            break;
                        case lessThan:
                            predicates.add(cb.lessThan(path, (Comparable) value));
                            break;
                        case lessThanOrEqualTo:
                            predicates.add(cb.lessThanOrEqualTo(path, (Comparable) value));
                            break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return specification;
    }

}
