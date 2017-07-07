package com.taylor.uuid.client.impl;

import com.taylor.common.utils.ThreadUtil;
import com.taylor.uuid.entity.BizIdEntity;
import com.taylor.uuid.entity.UidHolderStatus;
import com.taylor.uuid.exception.UidException;
import com.taylor.uuid.exception.UidSlotsEmptyException;
import com.taylor.uuid.loader.UidLoader;
import com.taylor.uuid.state.UidStateEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 持有所有id的内存状态
 *
 * @author Michael.Wang
 * @date 2017/5/2
 */
@Slf4j
public class NumHolder {
    private static Map<String, UidHolderStatus> currentIds = new ConcurrentHashMap<>();

    public static Map<String, UidHolderStatus> getCurrentIds() {
        return currentIds;
    }

    /**
     * 加载列表并初始化slots
     */
    public static void connectServerAndInitSlots() {
        List<BizIdEntity> bizIdEntities = UidLoader.fetchAll();
        initSlots(bizIdEntities);
    }

    /**
     * 初始化slots
     *
     * @param bizIdEntities
     */
    public static void initSlots(List<BizIdEntity> bizIdEntities) {
        try {
            //从远程加载全部记录
            for (BizIdEntity entity : bizIdEntities) {
                UidHolderStatus uidHolderStatus = new UidHolderStatus(entity);
                currentIds.put(entity.getBizType().toUpperCase(), uidHolderStatus);
            }
        } catch (Exception e) {
            log.error("fail to init slots", e);
        }
    }


    /**
     * 取下一个id
     *
     * @param bizTypeWithPostfix
     * @return
     */
    public static Long getNextId(String bizTypeWithPostfix) {
        for (int i = 0; i < 3; i++) {
            try {
                return getNextIdOnce(bizTypeWithPostfix);
            } catch (UidException e) {
                log.warn("fail to get next id for :{}", bizTypeWithPostfix, e);
                ThreadUtil.sleep(20);
            }
        }
        throw new UidException("Fail to get next Uid after trying 3 times");
    }

    /**
     * 取下一个id
     *
     * @param bizTypeWithPostfix
     * @return
     */
    private static Long getNextIdOnce(String bizTypeWithPostfix) {
        UidHolderStatus uidHolderStatus = getCurrentIds().get(bizTypeWithPostfix.toUpperCase());
        if (uidHolderStatus == null) {
            Set<String> keys = getCurrentIds().keySet();
            StringBuilder buffer = new StringBuilder();
            for (String key: keys){
                buffer.append(key).append(";");
            }
            log.error("Please init server context for bizTypeWithPostfix :{}. Current keys:{}",bizTypeWithPostfix,buffer.toString());
            throw new UidException("Please init server context for bizTypeWithPostfix :" + bizTypeWithPostfix);
        }

        if (uidHolderStatus.getUsingSlot().isEmpty()) {
            if (uidHolderStatus.getWaitingSlot().isEmpty()) {
                BufferUpdateThread.getThread().interrupt();
                throw new UidSlotsEmptyException();
            } else if (uidHolderStatus.getWaitingSlot().isFilled()) {
                uidHolderStatus.swapSlot();  //切换slot
                uidHolderStatus.getUsingSlot().setState(UidStateEnum.TAKING.getKey());
                long result = uidHolderStatus.getUsingSlot().getCurrentNum().incrementAndGet();
                if(result>uidHolderStatus.getUsingSlot().getMax()){
                    throw new UidException("Cannot get uid.UsingSlot is aboveMax.");
                }else{
                    return result;
                }
            } else if (uidHolderStatus.getWaitingSlot().isFilling()) {
                ThreadUtil.sleep(100);
                uidHolderStatus.swapSlot();  //切换slot
                long result = uidHolderStatus.getUsingSlot().getCurrentNum().incrementAndGet();
                if(result>uidHolderStatus.getUsingSlot().getMax()){
                    throw new UidException("Cannot get uid.UsingSlot is aboveMax.");
                }else{
                    return result;
                }
            } else {
                throw new UidException("Cannot get uid.UsingSlot is empty.");
            }
        } else if (uidHolderStatus.getUsingSlot().isFilling()) {
            throw new UidSlotsEmptyException();
        } else {
            //正常场景，获取nextId
            uidHolderStatus.getUsingSlot().setState(UidStateEnum.TAKING.getKey());
            return uidHolderStatus.getUsingSlot().getCurrentNum().incrementAndGet();
        }
    }
}
