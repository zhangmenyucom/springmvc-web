package com.taylor.annotation;

import com.taylor.common.JSONUtil;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/12/1 0:56
 */
public class AnnotaionAdvice implements AfterReturningAdvice{
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println(method.getName());
        System.out.println(JSONUtil.toJSONString(returnValue));
    }
}
