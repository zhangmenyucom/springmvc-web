package com.taylor.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/12/1 0:15
 */
public class SpringBeanUtils implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringBeanUtils.beanFactory = beanFactory;
    }
    public static BeanFactory getBeanFactory() {
        return beanFactory;
    }

    private static class FactoryUtilsHolder {
        private static final SpringBeanUtils factoryUtils = new SpringBeanUtils();
    }

    public static SpringBeanUtils getInstance(){
        return FactoryUtilsHolder.factoryUtils;
    }

    public static Object getBean(String name){
        return beanFactory.getBean(name);
    }
}
