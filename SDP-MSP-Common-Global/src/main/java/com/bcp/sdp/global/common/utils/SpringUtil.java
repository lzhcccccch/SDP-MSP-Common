package com.bcp.sdp.global.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @packageName： com.bcp.sdp.jpa.common.utils
 * @className: SpringUtil
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-14 10:37
 */
@Component
@Slf4j
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     *  实现 ApplicationContextAware 接口 重写 setApplicationContext() 方法 即可获取 ApplicationContext 对象
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("============= ApplicationContext 配置成功 =============");
        System.out.println("---------------------------------------------------------------------");
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     *  通过 bean 的 name 获取 Bean
     */
    public Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     *  通过 class 获取 bean
     */
    <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     *  通过 name 以及 Clazz 返回指定的 Bean
     */
    <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

}
