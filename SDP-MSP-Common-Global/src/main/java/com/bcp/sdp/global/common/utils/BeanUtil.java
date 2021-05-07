package com.bcp.sdp.global.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @packageName： com.bcp.sdp.jpa.common.utils
 * @className: BeanUtil
 * @description: Bean工具类,可将 bean 与 map 互转等操作. 使用 spring 内置的 cglib
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2021-01-14 10:59
 */
public class BeanUtil {

    /**
     * 复制Bean
     * @param source
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T>T copyProperties(Object source, Class<T> clazz){
        if(source == null){
            return null;
        }
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        copyProperties(source,t);
        return t;
    }


    public static void copyProperties(Object source, Object target) throws BeansException {
        copyProperties(source, target, null, (String[])null);
    }


    /**
     * 对象转换JSON方式
     * @param obj
     * @return
     */
    public static <T>T copyPropertiesJSON(Object obj,Class<T> clazz){
        JSONObject jsonObj = (JSONObject) JSON.toJSON(obj);
        T model = JSONObject.toJavaObject(jsonObj, clazz);
        return model;
    }


    private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class actualEditable = target.getClass();
        if(editable != null) {
            if(!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
            }

            actualEditable = editable;
        }

        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List ignoreList = ignoreProperties != null? Arrays.asList(ignoreProperties):null;
        PropertyDescriptor[] var7 = targetPds;
        int var8 = targetPds.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            PropertyDescriptor targetPd = var7[var9];
            Method writeMethod = targetPd.getWriteMethod();
            if(writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                //原字段找属性
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                //把目标字段改成下滑线命名规则
                if(sourcePd == null){
                    sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), BeanUtil.toUnderlineString(targetPd.getName()));
                }
                //把原字段改成驼峰命名规则
                if(sourcePd == null){
                    sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), BeanUtil.toCamelCaseString(targetPd.getName()));
                }
                if(sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if(readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if(!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }

                            Object ex = readMethod.invoke(source, new Object[0]);
                            if(!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }

                            writeMethod.invoke(target, new Object[]{ex});
                        } catch (Throwable var15) {
                            throw new FatalBeanException("Could not copy property \'" + targetPd.getName() + "\' from source to target", var15);
                        }
                    }
                }
            }
        }

    }

    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<Object, Object> beanToMap(T bean) {
        Map<Object, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            Set<Map.Entry<Object, Object>> entrySet = beanMap.entrySet();
            for (Map.Entry<Object, Object> entry : entrySet) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<Object, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     *
     * @param objList
     * @return
     */
    public static <T> List<Map<Object, Object>> objectsToMaps(List<T> objList) {
        List<Map<Object, Object>> list = new ArrayList<>();
        if (objList != null && objList.size() > 0) {
            Map<Object, Object> map = null;
            T bean = null;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> mapsToObjects(List<Map<Object, Object>> maps, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            Map<Object, Object> map = null;
            T bean = null;
            for (int i = 0, size = maps.size(); i < size; i++) {
                map = maps.get(i);
                bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * 复制List
     * @param list
     * @param c
     * @param <T1>
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static  <T1,T> List<T1> copyList(List<T> list, Class<T1> c) {
        List<T1> result_list = new ArrayList<>();

        for (T t:list
        ) {
            T1 t1 = null;
            try {
                t1 = c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            copyProperties(t,t1);
            result_list.add(t1);
        }
        return result_list;
    }



    private static final char SEPARATOR = '_';

    /**
     * 将属性样式字符串转成驼峰样式字符串<br>
     * (例:branchNo -> branch_no)<br>
     *
     * @param inputString
     * @return
     */
    public static String toUnderlineString(String inputString) {
        if (inputString == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            boolean nextUpperCase = true;

            if (i < (inputString.length() - 1)) {
                nextUpperCase = Character.isUpperCase(inputString.charAt(i + 1));
            }

            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) {
                        sb.append(SEPARATOR);
                    }
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 将驼峰字段转成属性字符串<br>
     * (例:branch_no -> branchNo )<br>
     *
     * @param inputString
     *            输入字符串
     * @return
     */
    public static String toCamelCaseString(String inputString) {
        return toCamelCaseString(inputString, false);
    }

    /**
     * 将驼峰字段转成属性字符串<br>
     * (例:branch_no -> branchNo )<br>
     *
     * @param inputString
     *            输入字符串
     * @param firstCharacterUppercase
     *            是否首字母大写
     * @return
     */
    public static String toCamelCaseString(String inputString, boolean firstCharacterUppercase) {
        if (inputString == null)
            return null;
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            switch (c) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;

                default:
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }

    /**
     * 得到标准字段名称
     *
     * @param inputString
     *            输入字符串
     * @return
     */
    public static String getValidPropertyName(String inputString) {
        String answer;
        if (inputString == null) {
            answer = null;
        } else if (inputString.length() < 2) {
            answer = inputString.toLowerCase(Locale.US);
        } else {
            if (Character.isUpperCase(inputString.charAt(0)) && !Character.isUpperCase(inputString.charAt(1))) {
                answer = inputString.substring(0, 1).toLowerCase(Locale.US) + inputString.substring(1);
            } else {
                answer = inputString;
            }
        }
        return answer;
    }

    /**
     * 将属性转换成标准set方法名字符串<br>
     *
     * @param property
     * @return
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "set");
        return sb.toString();
    }

    /**
     * 将属性转换成标准get方法名字符串<br>
     *
     * @param property
     * @return
     */
    public static String getGetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "get");
        return sb.toString();
    }

    //source中的非空属性复制到target中
    public static <T> void copyNotNullProperties(T source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        Set<String> emptyNames = getNullPropertyNameSet(source);
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
    public static Set<String> getNullPropertyNameSet(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames;
    }

    public static void main(String[] args) {
        //System.out.println(BeanUtils.toUnderlineString("oldMatNo"));
//		System.out.println(BeanUtils.getValidPropertyName("CertifiedStaff"));
//		System.out.println(BeanUtils.getSetterMethodName("userID"));
//		System.out.println(BeanUtils.getGetterMethodName("userID"));
//		System.out.println(BeanUtils.toCamelCaseString("iso_certified_staff", true));
//		System.out.println(BeanUtils.getValidPropertyName("certified_staff"));
//		System.out.println(BeanUtils.toCamelCaseString("site_Id"));
    }

}
