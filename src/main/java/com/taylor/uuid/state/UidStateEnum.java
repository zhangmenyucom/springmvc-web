package com.taylor.uuid.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Michael.Wang
 * @date 2017/5/2
 */
@Getter
@AllArgsConstructor
public enum UidStateEnum implements EnumCode<Integer> {
    EMPTY(0, "empty"), FILLING(1, "filling"), FILLED(2, "filled"), TAKING(3, "taking");

    private Integer key;
    private String description;
}
