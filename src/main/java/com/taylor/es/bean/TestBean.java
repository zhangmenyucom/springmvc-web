package com.taylor.es.bean;

import com.taylor.elasticsearch.EsIndexId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/5/19 11:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestBean {
    @EsIndexId
    private int id;
    private String name;
    private String contents;
}
