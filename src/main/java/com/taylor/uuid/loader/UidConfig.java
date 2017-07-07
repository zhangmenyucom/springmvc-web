package com.taylor.uuid.loader;

import com.taylor.common.utils.PropertyUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * Created by Michagel.Wang on 2017/5/4.
 */
@Slf4j
@Data
public class UidConfig {

    /**
     * 是否启用直接获取方式
     */
    public static boolean IS_DIRECT_ENABLED = true;

    /**
     * uid URL前缀
     */
    private String urlPrefixUid;

    /**
     * uid URL前缀
     */
    private String urlFetchAll;

    /**
     * 直接获取单个uid或编号
     */
    private String urlFetchDirect;

    /**
     * 扫描slot的时间间隔
     */
    private int scanIntervalSeconds = 1;

    private String module;

    private static UidConfig instance;

    /**
     * 初始化URL
     */
    public static UidConfig init(String urlContext, String module, int scanIntervalSeconds) {
        instance = new UidConfig();
        urlContext += urlContext.endsWith("/") ? "" : "/";
        instance.urlPrefixUid = urlContext + UidConstant.URL_UID;
        instance.urlFetchAll = urlContext + UidConstant.URL_FETCH_ALL_PART ;
        instance.urlFetchAll += instance.urlFetchAll.endsWith("/") ? "" : "/";
        instance.urlFetchAll += module;
        instance.urlFetchDirect = urlContext + UidConstant.URL_FETCH_DIRECT;
        instance.scanIntervalSeconds = scanIntervalSeconds;
        instance.module = module;
        return instance;
    }

    public static UidConfig getInstance() {
        return instance;
    }

    /**
     * 从属性文件中加载
     *
     * @return
     */
    public static Properties loadDefaultProperties() {
        try {
            Properties properties = PropertyUtil.getProperties(UidConstant.FILE_COMMON_PROPERTIES);
            String prefix = (String) properties.get(UidConstant.PROP_URL_PREFIX);
            prefix += prefix.endsWith("/") ? "" : "/";
            properties.setProperty(UidConstant.PROP_URL_PREFIX, prefix);
            return properties;
        } catch (Throwable t) {
            log.warn("fail to load properties", t);
            return new Properties();
        }
    }
}
