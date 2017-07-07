package com.taylor.listener;

import com.taylor.common.utils.StringUtil;
import com.taylor.uuid.client.UidGeneratorFacade;
import com.taylor.uuid.client.impl.BufferUpdateThread;
import com.taylor.uuid.loader.UidConfig;
import com.taylor.uuid.loader.UidConstant;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Properties;

/**
 * 配置示例：
 * <pre>
 * <context-param><param-name>module</param-name><param-value>order_center</param-value></context-param>
 * <listener><listener-class>com.weimob.sst.order.api.listener.UidGeneratorFacadeListener</listener-class></listener>
 * </pre>
 * <p>
 */
@Log4j2
public class IdGeneratorConfigListener implements ServletContextListener {

    private static final String MODULE = "module";

    /**
     * 是否从公共配置文件中获取配置信息
     */
    private boolean isGetPropertiesFromCommon = Boolean.TRUE;

    @Setter
    /** uid server的context,例如：http://localhost:8080/ */
    private String uidServerContext;
    @Setter
    /** 扫描空slot的时间间隔，以秒为单位 */
    private int scanIntervalSeconds = 1;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("IdGeneratorConfigListener contextInitialized start");
        String module = sce.getServletContext().getInitParameter(MODULE);
        if (StringUtil.isEmpty(module)) {
            throw new IllegalArgumentException("module cannot be null.please set module in web.xml");
        }

        if (isGetPropertiesFromCommon) {
            Properties properties = UidConfig.loadDefaultProperties();
            uidServerContext = properties.getProperty(UidConstant.PROP_URL_PREFIX);
            String scanIntervalSecondsConfig = properties.getProperty(UidConstant.SCAN_INTERVAL_SECONDS);
            if (StringUtil.isNotEmpty(scanIntervalSecondsConfig)) {
                scanIntervalSeconds = StringUtil.parseInt(scanIntervalSecondsConfig);
            }
        }

        if (StringUtil.isEmpty(uidServerContext)) {
            throw new IllegalArgumentException("uidServerContext cannot be null");
        }

        log.info("uidServerContext={},scanIntervalSeconds={}", uidServerContext, scanIntervalSeconds);
        UidGeneratorFacade.init(uidServerContext, module, scanIntervalSeconds);
        log.info("IdGeneratorConfigListener contextInitialized success");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        BufferUpdateThread.stop();
    }

}
