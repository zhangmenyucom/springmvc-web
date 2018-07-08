package com.taylor.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/12/1 0:21
 */
@Data
@Component
public class Demo {
    private int id;
    private String name;

    public Demo() {
        this.id = 1;
        this.name = "test";
    }
}
