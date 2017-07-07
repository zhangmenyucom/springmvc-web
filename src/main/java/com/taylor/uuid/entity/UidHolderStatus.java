package com.taylor.uuid.entity;

import com.taylor.uuid.state.UidStateEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Michael.Wang
 * @date 2017/5/2
 */
@Data
@Slf4j
public class UidHolderStatus {
    private String bizType;
    private NumStatus slotA;
    private NumStatus slotB;
    private volatile NumStatus usingSlot;
    private volatile NumStatus waitingSlot;

    public UidHolderStatus(BizIdEntity bizIdEntity) {
        int step = bizIdEntity.getStep();
        this.bizType = bizIdEntity.getBizType();
        long currentMax = bizIdEntity.getCurrentMax();

        this.slotA = new NumStatus();
        slotA.setBizType(bizType);
        slotA.setMax(currentMax - step);
        slotA.setCurrentNum(new AtomicLong(currentMax - step * 2));
        slotA.setStep(step);
        slotA.setState(UidStateEnum.FILLED.getKey());

        this.slotB = new NumStatus();
        slotB.setBizType(bizType);
        slotB.setMax(currentMax);
        slotB.setCurrentNum(new AtomicLong(currentMax - step));
        slotB.setStep(step);
        slotB.setState(UidStateEnum.FILLED.getKey());

        this.usingSlot = slotA;
        this.waitingSlot = slotB;
    }

    /**
     * 切换slot
     */
    public synchronized void swapSlot() {
        if (this.getWaitingSlot().isFilled()) {
            log.info("swap slot [start]. bizType is{}",this.bizType);
            NumStatus tmp = this.waitingSlot;
            this.waitingSlot = this.usingSlot;
            this.usingSlot = tmp;
            log.info("swap slot  [end]");
        }
    }
}
