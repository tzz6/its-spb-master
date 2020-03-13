package com.its.base.servers.es.rhl.impl;

import com.its.base.servers.es.rhl.ElasticSearchDml;
import com.its.common.es.rhl.annotation.ESMapping;
import com.its.common.es.rhl.enums.AggsType;
import com.its.common.es.rhl.enums.DataType;
import com.its.common.es.rhl.util.*;
import com.its.common.utils.bean.BeanTools;
import com.its.common.utils.json.JacksonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.min.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.percentiles.*;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Description: ElasticsearchIndexImpl
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2019/12/18 17:01
 */
@Service
public class ElasticSearDmlImpl<T, M> implements ElasticSearchDml<T, M> {
    private static final Log log = LogFactory.getLog(ElasticSearDmlImpl.class);
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public void setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public Response request(Request request) {
        try {
            return restHighLevelClient.getLowLevelClient().performRequest(request);
        } catch (IOException e) {
            log.error("ElasticSearDmlImpl.request:" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean save(T t) {
        try {
            MetaData metaData = IndexTools.getIndexType(t.getClass());
            String id = Tools.getESId(t);
            metaDataIsEmpty(metaData);
            IndexRequest indexRequest;
            String indexName = metaData.getIndexName();
            String indextype = metaData.getIndextype();
            if (StringUtils.isEmpty(id)) {
                indexRequest = new IndexRequest(indexName, indextype);
            } else {
                indexRequest = new IndexRequest(indexName, indextype, id);
            }
            String source = JacksonUtil.nonDefaultMapper().toJson(t);
            indexRequest.source(source, XContentType.JSON);
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                log.info("INDEX CREATE SUCCESS");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                log.info("INDEX UPDATE SUCCESS");
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.save:" + e.getMessage(), e);
        }
        return true;
    }

    @Override
    public BulkResponse bulkSave(List<T> list) {
        try {
            if (list == null || list.size() == 0) {
                return null;
            }
            T t = list.get(0);
            MetaData metaData = IndexTools.getIndexType(t.getClass());
            metaDataIsEmpty(metaData);
            String indexName = metaData.getIndexName();
            String indextype = metaData.getIndextype();
            BulkRequest bulkRequest = new BulkRequest();
            list.forEach(
                    obj -> {
                        try {
                            String id = Tools.getESId(obj);
                            IndexRequest indexRequest;
                            if (StringUtils.isEmpty(id)) {
                                indexRequest = new IndexRequest(indexName, indextype);
                            } else {
                                indexRequest = new IndexRequest(indexName, indextype, id);
                            }
                            bulkRequest.add(indexRequest.source(BeanTools.objectToMap(obj)));
                        } catch (Exception e) {
                            log.error("ElasticSearDmlImpl.Bulk.forEach:" + e.getMessage(), e);
                        }
                    }
            );
            return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.Bulk.save:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean update(T t) {
        try {
            String id = Tools.getESId(t);
            MetaData metaData = IndexTools.getIndexType(t.getClass());
            metaDataIdIsEmpty(id, metaData);
            UpdateRequest updateRequest = new UpdateRequest(metaData.getIndexName(), metaData.getIndextype(), id);
            updateRequest.doc(Tools.getFieldValue(t));
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
                log.info("INDEX CREATE SUCCESS");
            } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                log.info("INDEX UPDATE SUCCESS");
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.update:" + e.getMessage(), e);
        }
        return true;
    }

    @Override
    public boolean delete(T t) {
        try {
            String id = Tools.getESId(t);
            MetaData metaData = IndexTools.getIndexType(t.getClass());
            metaDataIdIsEmpty(id, metaData);
            DeleteRequest deleteRequest = new DeleteRequest(metaData.getIndexName(), metaData.getIndextype(), id);
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
                log.info("INDEX DELETE SUCCESS");
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.delete:" + e.getMessage(), e);
        }
        return true;
    }

    @Override
    public boolean deleteById(M id, Class<T> clazz) {
        try {
            MetaData metaData = IndexTools.getIndexType(clazz);
            metaDataIdIsEmpty(id, metaData);
            DeleteRequest deleteRequest = new DeleteRequest(metaData.getIndexName(), metaData.getIndextype(), id.toString());
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
                log.info("INDEX DELETE SUCCESS");
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.deleteById:" + e.getMessage(), e);
        }
        return true;
    }

    @Override
    public T getById(M id, Class<T> clazz) {
        MetaData metaData = IndexTools.getIndexType(clazz);
        try {
            metaDataIdIsEmpty(id, metaData);
            GetRequest getRequest = new GetRequest(metaData.getIndexName(), metaData.getIndextype(), id.toString());
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            if (getResponse.isExists()) {
                return JacksonUtil.nonDefaultMapper().fromJson(getResponse.getSourceAsString(), clazz);
            }
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.getById:" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<T> mgetById(M[] ids, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            MetaData metaData = IndexTools.getIndexType(clazz);
            metaDataIsEmpty(metaData);
            MultiGetRequest request = new MultiGetRequest();
            for (M id : ids) {
                request.add(new MultiGetRequest.Item(metaData.getIndexName(), metaData.getIndextype(), id.toString()));
            }
            MultiGetResponse multiGetResponse = restHighLevelClient.mget(request, RequestOptions.DEFAULT);
            for (MultiGetItemResponse itemResponse : multiGetResponse) {
                GetResponse getResponse = itemResponse.getResponse();
                if (getResponse.isExists()) {
                    list.add(JacksonUtil.nonDefaultMapper().fromJson(getResponse.getSourceAsString(), clazz));
                }
            }
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.mgetById:" + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean exists(M id, Class<T> clazz) {
        try {
            MetaData metaData = IndexTools.getIndexType(clazz);
            metaDataIdIsEmpty(id, metaData);
            GetRequest getRequest = new GetRequest(metaData.getIndexName(), metaData.getIndextype(), id.toString());
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            if (getResponse.isExists()) {
                return true;
            }
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.exists:" + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public SearchResponse search(SearchRequest searchRequest) {
        try {
            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("ElasticSearDmlImpl.search:" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<T> search(SearchSourceBuilder searchSourceBuilder, SearchType searchType, Class<T> clazz) {
        List<T> list = null;
        try {
            MetaData metaData = IndexTools.getIndexType(clazz);
            metaDataIsEmpty(metaData);
            list = new ArrayList<>();
            SearchRequest searchRequest = new SearchRequest(metaData.getIndexName());
            if (metaData.isPrintLog()) {
                log.info(searchSourceBuilder.toString());
            }
            searchRequest.source(searchSourceBuilder);
            searchRequest.searchType(searchType);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                T t = JacksonUtil.nonDefaultMapper().fromJson(hit.getSourceAsString(), clazz);
                list.add(t);
            }
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.search:" + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public long count(QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
//        String indexName = metaData.getIndexName();
//        String indextype = metaData.getIndextype();
//        CountRequest countRequest = new CountRequest(indexName);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(queryBuilder);
//        countRequest.source(searchSourceBuilder);
//        CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
//        long count = countResponse.getCount();
        return 0;
    }

    @Override
    public Aggregations searchAggregations(AggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, Class<T> clazz) {
        try {
            MetaData metaData = IndexTools.getIndexType(clazz);
            metaDataIsEmpty(metaData);
            String indexName = metaData.getIndexName();
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            if (queryBuilder != null) {
                searchSourceBuilder.query(queryBuilder);
            }
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggregationBuilder);
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            if (metaData.isPrintLog()) {
                log.info(searchSourceBuilder.toString());
            }
            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT).getAggregations();
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.searchAggregations:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Map<Object,Object> searchAggregations(String metricName, String bucketName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz) {
        Map<Object,Object> map = null;
        try {
            MetaData metaData = IndexTools.getIndexType(clazz);
            metaDataIsEmpty(metaData);
            Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
            Field fBucket = clazz.getDeclaredField(bucketName.replaceAll(keyword, ""));
            if (fMetric == null) {
                throw new Exception("metric field is null");
            }
            if (fBucket == null) {
                throw new Exception("bucket field is null");
            }
            metricName = genKeyword(fMetric, metricName);
            bucketName = genKeyword(fBucket, bucketName);

            String by = "by_" + bucketName;
            String me = aggsType.toString() + "_" + metricName;

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            TermsAggregationBuilder aggregation = AggregationBuilders.terms(by)
                    .field(bucketName);
            //默认按照聚合结果降序排序
            aggregation.order(BucketOrder.aggregation(me, false));
//        aggregation.order(BucketOrder.key(false));
            if (AggsType.count == aggsType) {
                aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
            } else if (AggsType.min == aggsType) {
                aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
            } else if (AggsType.max == aggsType) {
                aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
            } else if (AggsType.sum == aggsType) {
                aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
            } else if (AggsType.avg == aggsType) {
                aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
            }
            if (queryBuilder != null) {
                searchSourceBuilder.query(queryBuilder);
            }
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggregation);

            SearchRequest searchRequest = new SearchRequest(metaData.getIndexName());
            searchRequest.source(searchSourceBuilder);
            if (metaData.isPrintLog()) {
                log.info(searchSourceBuilder.toString());
            }
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            Aggregations aggregations = searchResponse.getAggregations();
            Terms byRiskCode = aggregations.get(by);
            //聚合结果集
            map = new LinkedHashMap<>(8);
            for (Terms.Bucket bucket : byRiskCode.getBuckets()) {
                if (AggsType.count == aggsType) {
                    ValueCount count = bucket.getAggregations().get(me);
                    long value = count.getValue();
                    map.put(bucket.getKey(), value);
                } else if (AggsType.min == aggsType) {
                    ParsedMin min = bucket.getAggregations().get(me);
                    double value = min.getValue();
                    map.put(bucket.getKey(), value);
                } else if (AggsType.max == aggsType) {
                    ParsedMax max = bucket.getAggregations().get(me);
                    double value = max.getValue();
                    map.put(bucket.getKey(), value);
                } else if (AggsType.sum == aggsType) {
                    ParsedSum sum = bucket.getAggregations().get(me);
                    double value = sum.getValue();
                    map.put(bucket.getKey(), value);
                } else if (AggsType.avg == aggsType) {
                    ParsedAvg avg = bucket.getAggregations().get(me);
                    double value = avg.getValue();
                    map.put(bucket.getKey(), value);
                }
            }
        } catch (Exception e) {
            log.error("ElasticSearDmlImpl.searchAggregations:" + e.getMessage(), e);
        }
        return map;
    }


    @Override
    public List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        return scroll(queryBuilder, clazz, Constant.DEFAULT_SCROLL_TIME);
    }

    @Override
    public List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz, long time) throws Exception {
        if (queryBuilder == null) {
            throw new NullPointerException();
        }
        MetaData metaData = IndexTools.getIndexType(clazz);
        metaDataIsEmpty(metaData);
        List<T> list = new ArrayList<>();
        Scroll scroll = new Scroll(TimeValue.timeValueHours(time));
        SearchRequest searchRequest = new SearchRequest(metaData.getIndexName());
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(Constant.DEFAULT_SCROLL_PERPAGE);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        //第一把查询出的结果
        for (SearchHit hit : searchHits) {
            T t = JacksonUtil.nonDefaultMapper().fromJson(hit.getSourceAsString(), clazz);
            list.add(t);
        }
        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHits) {
                T t = JacksonUtil.nonDefaultMapper().fromJson(hit.getSourceAsString(), clazz);
                list.add(t);
            }
        }
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse
                = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        boolean succeeded = clearScrollResponse.isSucceeded();
        return list;
    }


    @Override
    public List<Down> aggswith2level(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String... bucketNames) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        metaDataIsEmpty(metaData);
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        if (bucketNames == null) {
            throw new NullPointerException();
        }
        int length = 2;
        if (bucketNames.length != length) {
            throw new Exception("仅支持两层下钻聚合!");
        }
        Field[] fBuckets = new Field[bucketNames.length];
        for (int i = 0; i < bucketNames.length; i++) {
            fBuckets[i] = clazz.getDeclaredField(bucketNames[i].replaceAll(keyword, ""));
            if (fBuckets[i] == null) {
                throw new Exception("bucket field is null");
            }
        }
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = aggsType.toString() + "_" + metricName;

        String[] bys = new String[bucketNames.length];
        for (int i = 0; i < fBuckets.length; i++) {
            bucketNames[i] = genKeyword(fBuckets[i], bucketNames[i]);
            bys[i] = "by_" + bucketNames[i];
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder[] termsAggregationBuilders = new TermsAggregationBuilder[bucketNames.length];
        for (int i = 0; i < bucketNames.length; i++) {
            TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(bys[i]).field(bucketNames[i]);
            termsAggregationBuilders[i] = aggregationBuilder;
        }
        for (int i = 0; i < termsAggregationBuilders.length; i++) {
            if (i != termsAggregationBuilders.length - 1) {
                termsAggregationBuilders[i].subAggregation(termsAggregationBuilders[i + 1]);
            }
        }
        if (AggsType.count == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(termsAggregationBuilders[0]);
        SearchRequest searchRequest = new SearchRequest(metaData.getIndexName());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //下面不支持2层以上的下钻
        List<Down> downList = new ArrayList<>();
        Terms terms1 = searchResponse.getAggregations().get(bys[0]);
        Terms terms2;
        for (Terms.Bucket bucket : terms1.getBuckets()) {
            terms2 = bucket.getAggregations().get(bys[1]);
            for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                Down down = new Down();
                down.setLevel_1_key(bucket.getKey().toString());
                down.setLevel_2_key(bucket2.getKey().toString());
                if (AggsType.count == aggsType) {
                    ValueCount count = bucket2.getAggregations().get(me);
                    long value = count.getValue();
                    down.setValue(value);
                } else if (AggsType.min == aggsType) {
                    ParsedMin min = bucket2.getAggregations().get(me);
                    double value = min.getValue();
                    down.setValue(value);
                } else if (AggsType.max == aggsType) {
                    ParsedMax max = bucket2.getAggregations().get(me);
                    double value = max.getValue();
                    down.setValue(value);
                } else if (AggsType.sum == aggsType) {
                    ParsedSum sum = bucket2.getAggregations().get(me);
                    double value = sum.getValue();
                    down.setValue(value);
                } else if (AggsType.avg == aggsType) {
                    ParsedAvg avg = bucket2.getAggregations().get(me);
                    double value = avg.getValue();
                    down.setValue(value);
                }
                downList.add(down);
            }
        }
        return downList;
    }


    private static final String keyword = ".keyword";

    /**
     * 组织字段是否带有.keyword
     *
     * @param field
     * @param name
     * @return
     */
    private String genKeyword(Field field, String name) {
        ESMapping esMapping = field.getAnnotation(ESMapping.class);
        //带着.keyword直接忽略
        if (name == null || name.indexOf(keyword) > -1) {
            return name;
        }
        //只要keyword是true就要拼接
        //没配注解，但是类型是字符串，默认keyword是true
        if (esMapping == null) {
            if (field.getType() == String.class) {
                return name + keyword;
            }
        }
        //配了注解，但是类型是字符串，默认keyword是true
        else {
            if (esMapping.datatype() == DataType.text_type && esMapping.keyword() == true) {
                return name + keyword;
            }
        }
        return name;
    }

    @Override
    public double aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        metaDataIsEmpty(metaData);
        String me = aggsType.toString() + "_" + metricName;
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        if (AggsType.count == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.avg(me).field(metricName));
        }
        SearchRequest searchRequest = new SearchRequest(metaData.getIndexName());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        if (AggsType.count == aggsType) {
            ValueCount count = searchResponse.getAggregations().get(me);
            long value = count.getValue();
            return Double.parseDouble(String.valueOf(value));
        } else if (AggsType.min == aggsType) {
            ParsedMin min = searchResponse.getAggregations().get(me);
            return min.getValue();
        } else if (AggsType.max == aggsType) {
            ParsedMax max = searchResponse.getAggregations().get(me);
            return max.getValue();
        } else if (AggsType.sum == aggsType) {
            ParsedSum sum = searchResponse.getAggregations().get(me);
            return sum.getValue();
        } else if (AggsType.avg == aggsType) {
            ParsedAvg avg = searchResponse.getAggregations().get(me);
            return avg.getValue();
        }
        return 0d;
    }

    @Override
    public Stats statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        metaDataIsEmpty(metaData);
        String me = "stats";
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        StatsAggregationBuilder aggregation = AggregationBuilders.stats(me).field(metricName);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(metaData.getIndexName());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Stats stats = searchResponse.getAggregations().get(me);
        return stats;
    }

    @Override
    public Map<String, Stats> statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        metaDataIsEmpty(metaData);
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        Field fBucket = clazz.getDeclaredField(bucketName.replaceAll(keyword, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        if (fBucket == null) {
            throw new Exception("bucket field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        bucketName = genKeyword(fBucket, bucketName);

        String by = "by_" + bucketName;
        String me = "stats" + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregation = AggregationBuilders.terms(by)
                .field(bucketName);
        //默认按照count的降序排序
        aggregation.order(BucketOrder.count(false));
        aggregation.subAggregation(AggregationBuilders.stats(me).field(metricName));
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(metaData.getIndexName());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms byRiskCode = aggregations.get(by);
        Map<String, Stats> map = new LinkedHashMap<>();
        for (Terms.Bucket bucket : byRiskCode.getBuckets()) {
            Stats stats = bucket.getAggregations().get(me);
            map.put(bucket.getKey().toString(), stats);
        }
        return map;
    }


    @Override
    public long cardinality(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexName = metaData.getIndexName();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = "cardinality_" + metricName;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        CardinalityAggregationBuilder aggregation = AggregationBuilders
                .cardinality(me)
                .field(metricName);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Cardinality agg = searchResponse.getAggregations().get(me);
        return agg.getValue();
    }

    @Override
    public Map<Double, Double> percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        return percentilesAggs(metricName, queryBuilder, clazz, Constant.DEFAULT_PERCSEGMENT);
    }

    @Override
    public Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexName = metaData.getIndexName();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = "percentiles_" + metricName;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        PercentilesAggregationBuilder aggregation = AggregationBuilders.percentiles(me).field(metricName).percentiles(customSegment);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Map<Double, Double> map = new LinkedHashMap<>();
        Percentiles agg = searchResponse.getAggregations().get(me);
        for (Percentile entry : agg) {
            double percent = entry.getPercent();
            double value = entry.getValue();
            map.put(percent, value);
        }
        return map;
    }

    @Override
    public Map percentileRanksAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexName = metaData.getIndexName();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = "percentiles_" + metricName;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        PercentileRanksAggregationBuilder aggregation = AggregationBuilders.percentileRanks(me, customSegment).field(metricName);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Map<Double, Double> map = new LinkedHashMap<>();
        PercentileRanks agg = searchResponse.getAggregations().get(me);
        for (Percentile entry : agg) {
            double percent = entry.getPercent();
            double value = entry.getValue();
            map.put(percent, value);
        }
        return map;
    }


    @Override
    public Map filterAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, FiltersAggregator.KeyedFilter... filters) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexName = metaData.getIndexName();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (filters == null) {
            throw new NullPointerException();
        }
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        String me = aggsType.toString() + "_" + metricName;
        AggregationBuilder aggregation = AggregationBuilders.filters("filteragg", filters);
        searchSourceBuilder.size(0);
        if (AggsType.count == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Filters agg = searchResponse.getAggregations().get("filteragg");
        Map map = new LinkedHashMap();
        for (Filters.Bucket entry : agg.getBuckets()) {
            if (AggsType.count == aggsType) {
                ValueCount count = entry.getAggregations().get(me);
                long value = count.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.min == aggsType) {
                ParsedMin min = entry.getAggregations().get(me);
                double value = min.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.max == aggsType) {
                ParsedMax max = entry.getAggregations().get(me);
                double value = max.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.sum == aggsType) {
                ParsedSum sum = entry.getAggregations().get(me);
                double value = sum.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.avg == aggsType) {
                ParsedAvg avg = entry.getAggregations().get(me);
                double value = avg.getValue();
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    @Override
    public Map histogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, double interval) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexName = metaData.getIndexName();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        Field fBucket = clazz.getDeclaredField(bucketName.replaceAll(keyword, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        if (fBucket == null) {
            throw new Exception("bucket field is null");
        }
        metricName = genKeyword(fMetric, metricName);
        bucketName = genKeyword(fBucket, bucketName);
        String by = "by_" + bucketName;
        String me = aggsType.toString() + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregation = AggregationBuilders.histogram(by).field(bucketName).interval(interval);
        searchSourceBuilder.size(0);
        if (AggsType.count == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        ParsedHistogram agg = searchResponse.getAggregations().get(by);
        Map map = new LinkedHashMap();
        for (Histogram.Bucket entry : agg.getBuckets()) {
            if (AggsType.count == aggsType) {
                ValueCount count = entry.getAggregations().get(me);
                long value = count.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.min == aggsType) {
                ParsedMin min = entry.getAggregations().get(me);
                double value = min.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.max == aggsType) {
                ParsedMax max = entry.getAggregations().get(me);
                double value = max.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.sum == aggsType) {
                ParsedSum sum = entry.getAggregations().get(me);
                double value = sum.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.avg == aggsType) {
                ParsedAvg avg = entry.getAggregations().get(me);
                double value = avg.getValue();
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    @Override
    public Map dateHistogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, DateHistogramInterval interval) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexName = metaData.getIndexName();
        Field fMetric = clazz.getDeclaredField(metricName.replaceAll(keyword, ""));
        Field fBucket = clazz.getDeclaredField(bucketName.replaceAll(keyword, ""));
        if (fMetric == null) {
            throw new Exception("metric field is null");
        }
        if (fBucket == null) {
            throw new Exception("bucket field is null");
        } else if (fBucket.getType() != Date.class) {
            throw new Exception("bucket type is not support");
        }
        ESMapping esMapping = fBucket.getAnnotation(ESMapping.class);
        if (esMapping != null && esMapping.datatype() != DataType.date_type) {
            throw new Exception("bucket type is not support");
        }
        metricName = genKeyword(fMetric, metricName);
        bucketName = genKeyword(fBucket, bucketName);
        String by = "by_" + bucketName;
        String me = aggsType.toString() + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregation = AggregationBuilders.dateHistogram(by).field(bucketName).dateHistogramInterval(interval);
        searchSourceBuilder.size(0);
        if (AggsType.count == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.min == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.max == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.sum == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.avg == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        ParsedDateHistogram agg = searchResponse.getAggregations().get(by);
        Map map = new LinkedHashMap();
        for (Histogram.Bucket entry : agg.getBuckets()) {
            if (AggsType.count == aggsType) {
                ValueCount count = entry.getAggregations().get(me);
                long value = count.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.min == aggsType) {
                ParsedMin min = entry.getAggregations().get(me);
                double value = min.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.max == aggsType) {
                ParsedMax max = entry.getAggregations().get(me);
                double value = max.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.sum == aggsType) {
                ParsedSum sum = entry.getAggregations().get(me);
                double value = sum.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.avg == aggsType) {
                ParsedAvg avg = entry.getAggregations().get(me);
                double value = avg.getValue();
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }


    @Override
    public PageList<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        metaDataIsEmpty(metaData);
        PageList<T> pageList = new PageList<>();
        List<T> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(metaData.getIndexName());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        //分页
        searchSourceBuilder.from((pageSortHighLight.getCurrentPage() - 1) * pageSortHighLight.getPageSize());
        searchSourceBuilder.size(pageSortHighLight.getPageSize());
        //排序
        Sort sort = pageSortHighLight.getSort();
        List<Sort.Order> orders = sort.listOrders();
        orders.forEach(order ->
                searchSourceBuilder.sort(new FieldSortBuilder(order.getProperty()).order(order.getDirection()))
        );
        //高亮
        HighLight highLight = pageSortHighLight.getHighLight();
        boolean highLightFlag = false;
        if (highLight.getHighLightList() != null && highLight.getHighLightList().size() != 0) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            if (!StringUtils.isEmpty(highLight.getPreTag()) && !StringUtils.isEmpty(highLight.getPostTag())) {
                highlightBuilder.preTags(highLight.getPreTag());
                highlightBuilder.postTags(highLight.getPostTag());
            }
            for (int i = 0; i < highLight.getHighLightList().size(); i++) {
                highLightFlag = true;
                HighlightBuilder.Field highlightField = new HighlightBuilder.Field(highLight.getHighLightList().get(i));
                highlightBuilder.field(highlightField);
            }
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        searchRequest.source(searchSourceBuilder);
        if (metaData.isPrintLog()) {
            log.info(searchSourceBuilder.toString());
        }
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            T t = JacksonUtil.nonDefaultMapper().fromJson(hit.getSourceAsString(), clazz);
            //替换高亮字段
            if (highLightFlag) {
                Map<String, HighlightField> hmap = hit.getHighlightFields();
                hmap.forEach((k, v) ->
                        {
                            try {
                                Object obj = BeanTools.mapToObject(hmap, clazz);
                                BeanUtils.copyProperties(obj, t, BeanTools.getNoValuePropertyNames(obj));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
            list.add(t);
        }

        pageList.setList(list);
        pageList.setTotalElements(hits.totalHits);
        pageList.setTotalPages(getTotalPages(hits.totalHits, pageSortHighLight.getPageSize()));
        return pageList;
    }


    @Override
    public List<String> completionSuggest(String fieldName, String fieldValue, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        metaDataIsEmpty(metaData);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SuggestBuilder suggestBuilder = new SuggestBuilder();

        CompletionSuggestionBuilder completionSuggestionBuilder = new
                CompletionSuggestionBuilder(fieldName + ".suggest");
        completionSuggestionBuilder.text(fieldValue);
        completionSuggestionBuilder.size(Constant.COMPLETION_SUGGESTION_SIZE);
        suggestBuilder.addSuggestion("suggest_" + fieldName, completionSuggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);

        SearchRequest searchRequest = new SearchRequest(metaData.getIndexName());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        Suggest suggest = searchResponse.getSuggest();
        CompletionSuggestion completionSuggestion = suggest.getSuggestion("suggest_" + fieldName);
        List<String> list = new ArrayList<>();
        for (CompletionSuggestion.Entry entry : completionSuggestion.getEntries()) {
            for (CompletionSuggestion.Entry.Option option : entry) {
                String suggestText = option.getText().string();
                list.add(suggestText);
            }
        }
        return list;
    }

    private int getTotalPages(long totalHits, int pageSize) {
        return pageSize == 0 ? 1 : (int) Math.ceil((double) totalHits / (double) pageSize);
    }

    private void metaDataIdIsEmpty(Object id, MetaData metaData) throws Exception {
        if (metaData == null) {
            throw new Exception("metaData cannot be empty");
        }
        if (id == null || "".equals(id)) {
            throw new Exception("ID cannot be empty");
        }
    }

    private void metaDataIsEmpty(MetaData metaData) throws Exception {
        if (metaData == null) {
            throw new Exception("metaData cannot be empty");
        }
    }
}
