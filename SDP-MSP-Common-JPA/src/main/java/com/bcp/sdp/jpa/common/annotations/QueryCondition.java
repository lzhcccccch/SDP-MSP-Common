package com.bcp.sdp.jpa.common.annotations;

import com.bcp.sdp.jpa.common.enums.MatchType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface QueryCondition {

    String column() default "";

    MatchType func() default MatchType.equal;

    // object是否可以为null
    boolean nullable() default false;

    // 字符串是否可为空
    boolean emptyable() default false;

}
