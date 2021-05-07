package com.bcp.sdp.exception.aspects;

import com.bcp.sdp.exception.base.MyBaseErrorException;
import com.bcp.sdp.global.common.dto.exception.BaseErrorVo;
import com.bcp.sdp.global.common.dto.res.ResResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @packageName： com.bcp.sdp.exception.aspects
 * @className: ExceptionAspect
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-26 16:59
 */
@Aspect
@Component
@Order(-101)
@Slf4j
public class ExceptionAspect {

    @Pointcut("this(com.bcp.sdp.exception.controller.MyAbstractController) || " +
            "this(com.bcp.sdp.exception.service.MyService)")
    public void ExceptionPointCut() {

    }

    @Around("ExceptionPointCut()")
    public Object AroundMethod(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        try {
            result = point.proceed();
        } catch (MyBaseErrorException e) {
            log.error("自定义异常信息: {}" + e.getErrorMsg(), e);
            result = ResResult.setErrorResult(new BaseErrorVo(e.getErrorCode(), e.getErrorMsg()));
        } catch (RuntimeException e) {
            log.error("运行时异常信息: {}" + e.getMessage(), e);
            result = ResResult.setErrorResult(new BaseErrorVo(500, e.getMessage()));
        } catch (Exception e) {
            log.error("异常信息: {}" + e.getMessage(), e);
            result = ResResult.setErrorResult(new BaseErrorVo(501, e.getMessage()));
        }
        if (result instanceof ResResult) {
            ResResult res = (ResResult) result;
            return res;
        }
        return result;
    }

}
