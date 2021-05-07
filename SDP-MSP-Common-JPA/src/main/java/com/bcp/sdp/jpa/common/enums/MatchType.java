package com.bcp.sdp.jpa.common.enums;

/**
 * @packageName： com.bcp.sdp.jpa.common.enums
 * @className: MatchType
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-14 14:36
 */
public enum MatchType {

    equal,                  // filed = value
    //下面四个用于Number类型的比较
    gt,                     // filed > value
    ge,                     // field >= value
    lt,                     // field < value
    le,                     // field <= value
    notEqual,               // field != value
    like,                   // field like value
    notLike,                // field not like value
    // 下面四个用于可比较类型(Comparable)的比较
    greaterThan,            // field > value
    greaterThanOrEqualTo,   // field >= value
    lessThan,               // field < value
    lessThanOrEqualTo       // field <= value

}
