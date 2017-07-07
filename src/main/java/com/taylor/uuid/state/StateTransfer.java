package com.taylor.uuid.state;

import com.taylor.uuid.entity.NumStatus;
import com.taylor.uuid.exception.UidException;

import java.util.HashMap;
import java.util.Map;

public class StateTransfer {
    private static final Map states = new HashMap();

    static {
        //初始化状态迁移路径
        states.put(UidStateEnum.EMPTY, UidStateEnum.FILLING);
        states.put(UidStateEnum.FILLING, UidStateEnum.FILLED);
        states.put(UidStateEnum.FILLED, UidStateEnum.TAKING);
        states.put(UidStateEnum.TAKING, UidStateEnum.EMPTY);
    }

    public boolean canTransfer(NumStatus numStatus, UidStateEnum from, UidStateEnum to) {
        throw new UidException("Not Implemented");
    }
}
