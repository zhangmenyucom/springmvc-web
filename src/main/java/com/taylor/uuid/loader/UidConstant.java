package com.taylor.uuid.loader;


/**
 * @author taylor.zhang
 * @date 2017/5/2
 */
public class UidConstant {

    /**
     * controller中定义的mapping路径
     */
    public static final String URL_UID = "api/common/uid/generate/";

    /**
     * 获取模块全部id状态
     */
    public static final String URL_FETCH_ALL_PART = "api/common/uid/fetch";

    /**
     * 直接获取单个uid
     */
    public static final String URL_FETCH_DIRECT = "api/common/uid/direct/";

    /**
     * 成功的响应Code
     */
    public static final int SUCCESS_CODE = 0;
    /**
     * 属性文件名
     */
    public static final String FILE_COMMON_PROPERTIES = "common.properties";
    /**
     * 属性值
     */
    public static final String PROP_URL_PREFIX = "uid_url_prefix";

    /**
     * 模块
     **/
    public static final String MODULE = "module";

    /**
     * 扫描间隔
     **/
    public static final String SCAN_INTERVAL_SECONDS = "scan_interval_seconds";
}
