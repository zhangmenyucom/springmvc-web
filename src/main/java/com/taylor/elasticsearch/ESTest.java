package com.taylor.elasticsearch;

import com.taylor.es.bean.TestBean;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/5/19 10:39r
 */

@RunWith(JUnit4.class)
public class ESTest {
    private ElasticsearchService esService = new ElasticsearchService("elasticsearch", "192.168.186.130,192.168.186.131", "9300", "es", "test");

    @Test
    public void testSearch() {
        Map<String, Object> parameters = new HashMap<>();
        QueryBean testQueryBean = new QueryBean(parameters, 1, 3);
        FilterBuilder filterBuilder= FilterBuilders.boolFilter()
                .should(FilterBuilders.termFilter("name", "zhangmenyu"))
                .should(FilterBuilders.inFilter("contents", "15921624157"));
        QueryResult<TestBean> testBeanQueryResult = esService.queryObj(testQueryBean, TestBean.class, "test",filterBuilder);
    }

    @Test
    public void testInserInfoEs() {
        TestBean testBean=new TestBean();
        testBean.setId(2);
        testBean.setName("zhangmenyu");
        //testBean.setContents("15921624158");
        esService.indexObject(testBean);
    }
}
