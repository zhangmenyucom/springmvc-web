package com.taylor.uuid.entity;

import com.taylor.uuid.state.UidStateEnum;
import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Michael.Wang
 * @date 2017/5/2
 */
@Data
public class NumStatus {
    private String bizType;

    private int numType; //uid or biz num

    private AtomicLong currentNum;

    private long max;

    private int step;

    private int state;

    public boolean isEmpty() {
        int state = this.getState();
        if (state == UidStateEnum.TAKING.getKey() || state == UidStateEnum.EMPTY.getKey()) {
            if (this.getCurrentNum().longValue() >= this.getMax()) {
                this.setState(UidStateEnum.EMPTY.getKey());
                return true;
            }
        }
        return false;
    }

    public boolean isFilling(){
        return this.state == UidStateEnum.FILLING.getKey();
    }

    public boolean isFilled(){
        return this.state == UidStateEnum.FILLED.getKey();
    }

    public boolean isTaking(){
        return this.state == UidStateEnum.TAKING.getKey();
    }
}
