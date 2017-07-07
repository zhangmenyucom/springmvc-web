package com.taylor.common;

import com.taylor.common.utils.PropertyUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class PropertiesLoader {

    /**
     * 属性文件名
     */
    public static final String PROPERTIES_FILE = "redis.properties";
    public static final String UID_LOCK = "uid_lock_id";


    private static CommonResource resource;

    private static Properties properties;

    /**
     * 从属性文件中加载
     *
     * @return
     */
    public static CommonResource getResource() {
        if (resource == null) {
            synchronized (PropertiesLoader.class) {
                if (resource == null) {
                    init();
                }
            }
        }
        return resource;
    }

    /**
     * 获取锁Id
     *
     * @return
     */
    public static String getUidLockId() {
        return (String) properties.get(UID_LOCK);
    }

    /**
     * 从属性文件中加载
     *
     * @return
     */
    private static CommonResource init() {
        resource = new CommonResource();
        try {
            properties = PropertyUtil.getProperties(PROPERTIES_FILE);
            resource = new CommonResource();
            resource.setHost((String) properties.get("host"));
            resource.setPort(Integer.parseInt((String) properties.get("port")));
            resource.setInstance((String) properties.get("instance"));
            resource.setPassword((String) properties.get("password"));
        } catch (Throwable t) {
            log.warn("fail to load properties", t);
        }
        return resource;
    }
}
