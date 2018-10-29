package com.taylor.listener;/**
 * ${author} on 2018/10/26.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author zhangxiaolu
 * @描述
 * @since 2018/10/26 16:29
 */
public class TestListener implements HttpSessionListener, ServletRequestListener, ServletRequestAttributeListener {
    private Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        logger.info(".......TestListener sessionCreated().......");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
        logger.info(".......TestListener sessionDestroyed().......");
    }


    @Override
    public void requestInitialized(ServletRequestEvent arg0) {
        logger.info("......TestListener requestInitialized()......");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent arg0) {
        logger.info("......TestListener requestDestroyed()......");
    }

    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        logger.info("......TestListener attributeAdded()......");
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        logger.info("......TestListener attributeRemoved()......");
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        logger.info("......TestListener attributeReplaced()......");
    }
}