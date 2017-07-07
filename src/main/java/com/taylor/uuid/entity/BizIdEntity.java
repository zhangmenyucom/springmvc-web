package com.taylor.uuid.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Michael.Wang
 * @date 2017/4/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizIdEntity {
    private int id;

    private String bizType;

    private long currentMax;

    private int step;

    private Timestamp updateTime;
}
