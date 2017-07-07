package com.taylor.uuid.client;


import com.taylor.uuid.client.impl.BufferUpdateThread;
import com.taylor.uuid.client.impl.BufferedUidGenerator;
import com.taylor.uuid.client.impl.NumHolder;
import com.taylor.uuid.loader.UidConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Michael.Wang
 * @date 2017/4/24
 */
@Slf4j
public class UidGeneratorFacade {

    public static final String POST_FIX_ID = "_ID";
    public static final String POST_FIX_NO = "_NO";

    private static UidGenerator uidGenerator;

    private static Boolean isInited = false;

    /**
     * 使用前务必调用此方法进行初始化，如果在容器中使用，需要在Listener中进行初始化
     *
     * @param uidServerContext    uid server的context,例如：http://localhost:8080/
     * @param scanIntervalSeconds 扫描空slot的时间间隔，以秒为单位
     */
    public static synchronized void init(String uidServerContext, String module, int scanIntervalSeconds) {
        if (!isInited) {
            UidConfig.init(uidServerContext, module, scanIntervalSeconds);
            //从远程加载全部记录
            NumHolder.connectServerAndInitSlots();
            Thread thread = BufferUpdateThread.getThread();
            try {
                thread.setDaemon(true);
            } catch (Exception e) {
                log.warn("fail to set deamon.", e);
            }
            if (!thread.isAlive()&&!thread.isInterrupted()) {
                thread.start();
            }
            isInited = true;
        }
    }

    /**
     * @param bizType 业务类型（表名）
     * @return 下一个uid
     */
    public static long getUID(String bizType) {
        return getInstance().getUID(bizType.toUpperCase() + POST_FIX_ID);
    }

    /**
     * @param bizType 业务类型（表名）
     * @param number 数量
     * @return 下一个uid
     */
    public static long[] getUIDs(String bizType, int number) {
        return getInstance().getUIDs(bizType.toUpperCase() + POST_FIX_ID, number);
    }

    /**
     * @param bizType
     * @return
     */
    public static long getUNO(String bizType) {
        return getInstance().getUID(bizType.toUpperCase() + POST_FIX_NO);
    }

    /**
     * @param bizType
     * @return
     */
    public static long[] getUNOs(String bizType, int number) {
        return getInstance().getUIDs(bizType.toUpperCase() + POST_FIX_NO, number);
    }

    public static UidGenerator getInstance() {
        if (uidGenerator == null) {//double check
            synchronized (UidGeneratorFacade.class) {
                if (uidGenerator == null) {
                    uidGenerator = new BufferedUidGenerator();
                }
            }
        }
        return uidGenerator;
    }
}
