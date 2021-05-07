package com.bcp.sdp.exception.aspects;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @packageName： com.bcp.sdp.exception.aspects
 * @className: LoggerAspect
 * @description:  日志打印
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-02-01 22:42
 */
@Aspect
@Component
@Order(-100)
@Slf4j
public class LoggerAspect {

    // 行分隔符(即换行) 会根据不同系统(Windows/Linux..)生成相应的换行符
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Pointcut("this(com.bcp.sdp.exception.controller.MyAbstractController)")
    public void logPointCut() {

    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        // RequestContextHolder 请求上下文控制器(里面用两个 ThreadLocal 保存请求, 在 mvc 的那一套流程中给设置进去的值
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("========================================== Start ==========================================");
        log.info("请求 Url: {}" + request.getRequestURL().toString());
        log.info("请求方式: {}" + request.getMethod());
        log.info("请求入参: {}" + JSON.toJSONString(joinPoint.getArgs()));
        log.info("请求方法: {}" + joinPoint.getSignature().getDeclaringTypeName());
        log.info("方法路径: {}" + joinPoint.getSignature().getName());
        log.info("IP: {}" + request.getRemoteAddr());
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        log.info("返回结果  : {}", JSON.toJSONString(result));
        log.info("执行耗时 : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

    @After("logPointCut()")
    public void doAfter() {
        log.info("=========================================== End ===========================================" + LINE_SEPARATOR);
    }

}
