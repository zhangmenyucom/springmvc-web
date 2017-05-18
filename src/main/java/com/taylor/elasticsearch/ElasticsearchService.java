/**
 *
 */
package com.taylor.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.ValueType;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author haoli
 */
@Log4j
/*
 * Im Message index and query service provid by Elasticsearch Server.
 */
public class ElasticsearchService {
    private String clusterName;
    private String nodeAddress;
    private String nodePort;
    private String indexName;
    private String indexType;

    public Client esClient = null;
    private BulkProcessor bulkProcessor = null;
    private ObjectMapper mapper = new ObjectMapper();

    public ElasticsearchService(String clusterNameP, String nodeAddressP, String nodePortP, String index, String type) {
        clusterName = clusterNameP;
        nodeAddress = nodeAddressP;
        nodePort = nodePortP;
        indexName = index;
        indexType = type;
        Settings settings = ImmutableSettings.settingsBuilder()
                // Below line will cause connect error.
                // .put("client.transport.sniff", true)
                .put("cluster.name", clusterName).build();
        esClient = new TransportClient(settings);
        for (String address : nodeAddress.split(",")) {
            for (String port : nodePort.split(",")) {
                ((TransportClient) esClient).addTransportAddress(new InetSocketTransportAddress(
                        address, Integer.valueOf(port)));
            }
        }

        bulkProcessor = BulkProcessor
                .builder(esClient, new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request, BulkResponse response) {
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request, Throwable failure) {
                    }
                }).setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1).build();
    }

    @Override
    protected void finalize() {
        if (esClient != null) {
            esClient.close();
        }
        if (bulkProcessor != null) {
            bulkProcessor.flush();
            bulkProcessor.close();
        }
    }

    public <T> boolean indexObject(T item) {
        return this.indexObject(item, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.weimob.imuc.thirdpart.service.ImUcMsgEsService#indexMsg(com.weimob
     * .imuc.thirdpart.model.MessageIndexItem)
     */
    public <T> boolean indexObject(T item, String type) {
        log.info("Start to send item to ES server.");
        try {
            IndexResponse response = esClient
                    .prepareIndex(indexName, type == null ? this.indexType : type, getId((Object) item))
                    .setSource(mapper.writeValueAsString(item)).execute()
                    .actionGet();
            log.info("End to send item to ES server." + JSON.toJSONString(response));
            if (StringUtils.isNotEmpty(response.getId())) {
                return true;
            }
        } catch (ElasticsearchException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private String getId(Object item) {
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EsIndexId.class)) {
                try {
                    field.setAccessible(true);
                    return String.valueOf(field.get(item));
                } catch (IllegalArgumentException e) {

                    e.printStackTrace();
                } catch (IllegalAccessException e) {

                    e.printStackTrace();
                }
            }
        }
        throw new IllegalArgumentException("Missing EsIndexId annotation on pass-in item.");
    }

    public <T> boolean indexItem(List<T> msgs) {
        return this.indexItem(msgs, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.weimob.imuc.thirdpart.service.ImUcMsgEsService#indexMsg(java.util
     * .List)
     */
    public <T> boolean indexItem(List<T> msgs, String type) {

        for (T item : msgs) {
            try {
                bulkProcessor.add(new IndexRequest(indexName, type == null ? indexType : type, getId(item)).source(mapper.writeValueAsString(item)));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void deleteIndexItem(String id) {
        this.deleteIndexItem(id, null);
    }

    public void deleteIndexItem(String id, String type) {
        bulkProcessor.add(new DeleteRequest(indexName, type == null ? indexType : type, id));
    }

    public <T> QueryResult<T> queryObj(QueryBean queryBean, Class<T> clazz) {
        return queryObj(queryBean, clazz, null);
    }

    public <T> QueryResult<T> queryObj(QueryBean queryBean, Class<T> clazz, String type) {
        QueryResult<T> result = new QueryResult<T>();
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        for (String key : queryBean.getParameters().keySet()) {
            if (queryBean.getParameters().get(key) instanceof String) {
                qb.must(QueryBuilders.matchQuery(key, queryBean.getParameters().get(key)));
            } else {
                qb.must(QueryBuilders.termQuery(key, queryBean.getParameters().get(key)));
            }
        }
        int actPage = (queryBean.getPageFrom() - 1);
        SearchResponse response = esClient.prepareSearch(indexName)
                .setTypes(type == null ? indexType : type).setQuery(qb)
                .setSize(queryBean.getPageSize())
                .setFrom(actPage * queryBean.getPageSize())
                .execute().actionGet();
        List<T> objList = new ArrayList<T>();
        if (response != null && response.getHits() != null
                && response.getHits().getHits() != null) {
            for (SearchHit hit : response.getHits().getHits()) {
                try {
                    objList.add(mapper.readValue(hit.getSourceAsString(), clazz));
                } catch (JsonParseException e) {

                    e.printStackTrace();
                } catch (JsonMappingException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
        result.setTotalCount(response.getHits().getTotalHits());
        result.setPageFrom(queryBean.getPageFrom());
        result.setPageSize(queryBean.getPageSize());
        result.setItems(objList);
        return result;
    }

    /**
     * filterBuilder为查询区间
     *
     * @param queryBean
     * @param clazz
     * @param type
     * @param filterBuilder
     * @return
     */
    public <T> QueryResult<T> queryObj(QueryBean queryBean, Class<T> clazz, String type, FilterBuilder filterBuilder) {
        QueryResult<T> result = new QueryResult<T>();
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        for (String key : queryBean.getParameters().keySet()) {
            if (queryBean.getParameters().get(key) instanceof String) {
                qb.must(QueryBuilders.matchQuery(key, queryBean.getParameters().get(key)));
            } else {
                qb.must(QueryBuilders.termQuery(key, queryBean.getParameters().get(key)));
            }
        }
        int actPage = (queryBean.getPageFrom() - 1);
        SearchRequestBuilder builder = esClient.prepareSearch(indexName)
                .setTypes(type == null ? indexType : type)
                .setQuery(qb)
                .setSize(queryBean.getPageSize())
                .setFrom(actPage * queryBean.getPageSize());
        if (filterBuilder != null) {
            builder = builder.setPostFilter(filterBuilder);
        }
        if (StringUtils.isNotBlank(queryBean.getSortFiled())) {
            builder = builder.addSort(queryBean.getSortFiled(), queryBean.getSortOrder());
        }
        SearchResponse response = builder.execute().actionGet();
        List<T> objList = new ArrayList<T>();
        if (response != null && response.getHits() != null
                && response.getHits().getHits() != null) {
            for (SearchHit hit : response.getHits().getHits()) {
                try {
                    objList.add(mapper.readValue(hit.getSourceAsString(), clazz));
                } catch (JsonParseException e) {

                    e.printStackTrace();
                } catch (JsonMappingException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
        result.setTotalCount(response.getHits().getTotalHits());
        result.setPageFrom(queryBean.getPageFrom());
        result.setPageSize(queryBean.getPageSize());
        result.setItems(objList);
        return result;
    }

    public List<AggregationResult> getAggregationResult(QueryBean queryBean, ValueType groupByType) {
        List<AggregationResult> result = new ArrayList<AggregationResult>();
        FilterBuilder filterBuilder = this.getFilterBuilder(queryBean);
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        setQBMust(queryBean, qb);
        SearchRequestBuilder builder = esClient.prepareSearch(indexName)
                .setTypes(queryBean.getType() == null ? indexType : queryBean.getType()).setQuery(qb)
                .setSize(0);
        if (filterBuilder != null) {
            builder = builder.setPostFilter(filterBuilder);
        }
        if (StringUtils.isNotBlank(queryBean.getSortFiled())) {
            builder = builder.addSort(queryBean.getSortFiled(), queryBean.getSortOrder());
        }
        TermsBuilder termsBuilder = null;
        String aggregationName = null;
        if (StringUtils.isNotBlank(queryBean.getGroupByFiled())) {
            termsBuilder = new TermsBuilder("group");
            termsBuilder.field(queryBean.getGroupByFiled());
            aggregationName = "group";
        }
        SumBuilder sumBuilder = null;
        if (StringUtils.isNotBlank(queryBean.getSumFiled())) {
            sumBuilder = new SumBuilder("sum");
            sumBuilder.field(queryBean.getSumFiled());
            if (StringUtils.isBlank(aggregationName)) {
                aggregationName = "sum";
            }
        }
        if (termsBuilder != null) {
            if (sumBuilder != null) {
                termsBuilder.subAggregation(sumBuilder);
            }
            termsBuilder.size(Integer.MAX_VALUE);
            builder.addAggregation(termsBuilder);
        } else {
            if (sumBuilder != null) {
                builder.addAggregation(sumBuilder);
            }
        }
        SearchResponse response = builder.execute().actionGet();
        Aggregation aggregation = response.getAggregations().get(aggregationName);
        if (aggregationName.equals("sum")) {
            InternalSum sum = (InternalSum) aggregation;
            AggregationResult aggregationResult = new AggregationResult();
            aggregationResult.setSum(sum.getValue());
            result.add(aggregationResult);
            return result;
        }

        List<Bucket> buckets = null;
        if (groupByType == ValueType.STRING) {
            StringTerms terms = (StringTerms) aggregation;
            buckets = terms.getBuckets();
        } else if (groupByType == ValueType.LONG) {
            LongTerms terms = (LongTerms) aggregation;
            buckets = terms.getBuckets();
        }
        for (Bucket bucket : buckets) {
            AggregationResult aggregationResult = new AggregationResult();
            String key = bucket.getKey();
            long docCount = bucket.getDocCount();
            if (StringUtils.isNotBlank(queryBean.getSumFiled())) {
                InternalSum internalSum = bucket.getAggregations().get("sum");
                double sum = internalSum.getValue();
                aggregationResult.setSum(sum);
            }
            aggregationResult.setKey(key);
            aggregationResult.setCount(docCount);
            result.add(aggregationResult);
        }
        return result;
    }

    private void setQBMust(QueryBean queryBean, BoolQueryBuilder qb) {
        if (queryBean.getParameters() != null) {
            for (String key : queryBean.getParameters().keySet()) {
                if (queryBean.getParameters().get(key) instanceof String) {
                    qb.must(QueryBuilders.matchQuery(key, queryBean.getParameters().get(key)));
                } else {
                    qb.must(QueryBuilders.termQuery(key, queryBean.getParameters().get(key)));
                }
            }
        }
        if (queryBean.getMustParams() != null) {
            for (String key : queryBean.getMustParams().keySet()) {
                if (queryBean.getMustParams().get(key) instanceof String) {
                    qb.must(QueryBuilders.matchPhraseQuery(key, queryBean.getMustParams().get(key)).slop(0));
                } else {
                    qb.must(QueryBuilders.termQuery(key, queryBean.getMustParams().get(key)));
                }
            }
        }
    }

    public <T> QueryResult<T> queryByQueryBean(QueryBean queryBean, Class<T> clazz) {
        log.info(clusterName + "#" + nodeAddress + "#" + nodePort + "#" + indexName + "#" + indexType);
        log.info(JSON.toJSONString(queryBean));
        FilterBuilder filterBuilder = this.getFilterBuilder(queryBean);
        QueryResult<T> result = new QueryResult<T>();
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        setQBMust(queryBean, qb);
        int actPage = (queryBean.getPageFrom() - 1);
        SearchRequestBuilder builder = esClient.prepareSearch(indexName)
                .setTypes(queryBean.getType() == null ? indexType : queryBean.getType())
                .setQuery(qb).setSize(queryBean.getPageSize()).setFrom(actPage * queryBean.getPageSize());
        if (filterBuilder != null) {
            builder = builder.setPostFilter(filterBuilder);
        }
        if (StringUtils.isNotBlank(queryBean.getSortFiled())) {
            builder = builder.addSort(queryBean.getSortFiled(), queryBean.getSortOrder());
            if (CollectionUtils.isNotEmpty(queryBean.getSortOrderExts())) {
                for (SortOrderExt ext : queryBean.getSortOrderExts()) {
                    builder = builder.addSort(ext.getSortFiled(), ext.getSortOrder());
                }
            }
        }
        SearchResponse response = builder.execute().actionGet();
        List<T> objList = new ArrayList<T>();
        if (response != null && response.getHits() != null && response.getHits().getHits() != null) {
            for (SearchHit hit : response.getHits().getHits()) {
                try {
                    objList.add(mapper.readValue(hit.getSourceAsString(), clazz));
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    log.error(e);
                    e.printStackTrace();
                }
            }
        }
        result.setTotalCount(response.getHits().getTotalHits());
        result.setPageFrom(queryBean.getPageFrom());
        result.setPageSize(queryBean.getPageSize());
        result.setItems(objList);
        return result;
    }

    private FilterBuilder getFilterBuilder(QueryBean queryBean) {
        FilterBuilder filterBuilder = null;
        if (CollectionUtils.isNotEmpty(queryBean.getTermFilters())) {
            for (EsTermsFilter filter : queryBean.getTermFilters()) {
                if (CollectionUtils.isNotEmpty(filter.getTerms())) {
                    TermsFilterBuilder termsFilter = FilterBuilders.termsFilter(filter.getFiledName(),
                            filter.getTerms());
                    filterBuilder = this.andFilter(filterBuilder, termsFilter);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(queryBean.getRangeFilters())) {
            for (ESRangeFilter filter : queryBean.getRangeFilters()) {
                RangeFilterBuilder rangeFilter = FilterBuilders.rangeFilter(filter.getFiledName());
                if (filter.getGt() != null) {
                    rangeFilter.gt(filter.getGt());
                }
                if (filter.getLt() != null) {
                    rangeFilter.lt(filter.getLt());
                }
                if (filter.getFrom() != null) {
                    rangeFilter.from(filter.getFrom());
                }
                if (filter.getTo() != null) {
                    rangeFilter.to(filter.getTo());
                }
                filterBuilder = this.andFilter(filterBuilder, rangeFilter);
            }
        }
        return filterBuilder;
    }

    private FilterBuilder andFilter(FilterBuilder filterBuilder, FilterBuilder addfilter) {
        if (filterBuilder == null) {
            filterBuilder = addfilter;
        } else {
            filterBuilder = FilterBuilders.andFilter(filterBuilder, addfilter);
        }
        return filterBuilder;
    }
}
