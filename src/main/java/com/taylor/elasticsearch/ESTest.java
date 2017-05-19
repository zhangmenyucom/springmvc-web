package com.taylor.elasticsearch;

import com.taylor.es.bean.TestBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/5/19 10:39r
 */
public class ESTest {
    public static void main(String[] args) {
        ElasticsearchService esService=new ElasticsearchService("elasticsearch","192.168.186.130","9300","es","test");
        Map<String, Object> parameters=new HashMap<>();
        parameters.put("name","zhangmenyu");
        QueryBean testQueryBean=new QueryBean(parameters,1,3);
        QueryResult<TestBean> testBeanQueryResult = esService.queryObj(testQueryBean, TestBean.class,"test");
       // System.out.println(testBeanQueryResult);
    }
}
