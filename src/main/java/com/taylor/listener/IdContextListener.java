package com.taylor.listener;

import com.taylor.common.utils.ThreadUtil;
import com.taylor.service.UidGateWayService;
import com.taylor.uuid.client.impl.BufferUpdateThread;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 配置示例：
 * <pre>
 * <context-param><param-name>module</param-name><param-value>order_center</param-value></context-param>
 * <listener><listener-class>com.weimob.sst.order.api.listener.UidGeneratorFacadeListener</listener-class></listener>
 * </pre>
 * <p>
 * Created by xiongmiao on 2017/5/5.
 */
@Log4j2
public class IdContextListener implements ServletContextListener {
    /**
     * 扫描空slot的时间间隔，以秒为单位
     */
    private int scanIntervalSeconds = 1;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        UidGateWayService uidGateWayService = ctx.getBean(UidGateWayService.class);
        while (uidGateWayService == null) {
            log.debug("uidGateWayService is null. Sleep 5000 ms.");
            ThreadUtil.sleep(5000);
            uidGateWayService = ctx.getBean(UidGateWayService.class);
        }
        log.info("init uid gateway [start]");
        uidGateWayService.init();
        log.info("init uid gateway [end]");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        BufferUpdateThread.stop();
    }

}
