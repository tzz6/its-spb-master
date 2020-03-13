package com.its.base.servers.es.rhl;

import com.its.common.es.rhl.enums.AggsType;
import com.its.common.es.rhl.util.Down;
import com.its.common.es.rhl.util.PageList;
import com.its.common.es.rhl.util.PageSortHighLight;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;

/**
 * Description: ElasticsearchBase
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2019/12/18 17:28
 */
public interface ElasticSearchDml<T, M> {

    /**
     * 通过Low Level REST Client 查询
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.6/java-rest-low-usage-requests.html
     *
     * @param request org.elasticsearch.client.Request
     * @return org.elasticsearch.client.Response
     * @throws Exception ex
     */
    Response request(Request request);


    /**
     * 新增索引数据(重复保存覆盖更新索引)
     *
     * @param t 索引数据
     * @return boolean
     */
    boolean save(T t);


    /**
     * 批量新增索引数据
     *
     * @param list 索引数据集
     * @return BulkResponse
     */
    BulkResponse bulkSave(List<T> list);

    /**
     * 按照有值字段更新索引
     *
     * @param t 对象
     * @return boolean
     */
    boolean update(T t);

    /**
     * 删除索引
     *
     * @param t 对象
     * @return boolean
     */
    boolean delete(T t);

    /**
     * 删除索引
     *
     * @param id    id
     * @param clazz clazz
     * @return boolean
     */
    boolean deleteById(M id, Class<T> clazz);

    /**
     * 根据ID查询
     *
     * @param id    id
     * @param clazz clazz
     * @return T
     */
    T getById(M id, Class<T> clazz);

    /**
     * 根据ID列表批量查询
     *
     * @param ids   ids
     * @param clazz clazz
     * @return List<T>
     */
    List<T> mgetById(M[] ids, Class<T> clazz);

    /**
     * id数据是否存在
     *
     * @param id    id
     * @param clazz clazz
     * @return boolean
     */
    boolean exists(M id, Class<T> clazz);

    /**
     * 【最原始】查询
     *
     * @param searchRequest searchRequest
     * @return SearchResponse
     */
    SearchResponse search(SearchRequest searchRequest);

    /**
     * TODO 这里回头有时间研究一下jpa的原理，如何能够直接给interface生成代理类，并托管spring
     * 非分页查询
     * 目前暂时传入类类型
     *
     * @param searchSourceBuilder searchSourceBuilder
     * @param searchType          searchType
     * @param clazz               clazz
     * @return List<T>
     */
    List<T> search(SearchSourceBuilder searchSourceBuilder, SearchType searchType, Class<T> clazz);

    /**
     * 查询数量
     *
     * @param queryBuilder queryBuilder
     * @param clazz        clazz
     * @return
     * @throws Exception
     */
    public long count(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 支持分页、高亮、排序的查询
     *
     * @param queryBuilder      queryBuilder
     * @param pageSortHighLight pageSortHighLight
     * @param clazz             clazz
     * @return PageList<T>
     * @throws Exception
     */
    public PageList<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight, Class<T> clazz) throws Exception;

    /**
     * 通用（定制）聚合基础方法
     *
     * @param aggregationBuilder 聚合构建
     * @param queryBuilder       查询条件
     * @param clazz              clazz
     * @return Aggregations
     */
    public Aggregations searchAggregations(AggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, Class<T> clazz);

    /**
     * 普通聚合查询
     * 以bucket分组以aggstypes的方式metric度量
     *
     * @param metricName   聚合字段
     * @param bucketName   分组字段
     * @param aggsType     聚合类型
     * @param queryBuilder 查询条件
     * @param clazz        clazz
     * @return 聚合查询结果集
     */
    public Map<Object, Object> searchAggregations(String metricName, String bucketName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz);

    /**
     * scroll方式查询(默认了保留时间为Constant.DEFAULT_SCROLL_TIME)
     *
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    public List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * scroll方式查询
     *
     * @param queryBuilder
     * @param clazz
     * @param time         保留小时
     * @return
     * @throws Exception
     */
    public List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz, long time) throws Exception;


    /**
     * 搜索建议
     *
     * @param fieldName
     * @param fieldValue
     * @param clazz
     * @return
     * @throws Exception
     */
    public List<String> completionSuggest(String fieldName, String fieldValue, Class<T> clazz) throws Exception;


    /**
     * 以aggstypes的方式metric度量
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    public double aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;


    /**
     * 下钻聚合查询(无排序默认策略)
     * 以bucket分组以aggstypes的方式metric度量
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketNames
     * @return
     * @throws Exception
     */
    public List<Down> aggswith2level(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String... bucketNames) throws Exception;


    /**
     * 统计聚合metric度量
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    public Stats statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 以bucket分组，统计聚合metric度量
     *
     * @param bucketName
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    public Map<String, Stats> statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception;


    /**
     * 基数查询
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    public long cardinality(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 百分比聚合 默认聚合见Constant.DEFAULT_PERCSEGMENT
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    public Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 以百分比聚合
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param customSegment
     * @return
     * @throws Exception
     */
    public Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception;


    /**
     * 以百分等级聚合 (统计在多少数值之内占比多少)
     *
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param customSegment
     * @return
     * @throws Exception
     */
    public Map percentileRanksAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception;


    /**
     * 过滤器聚合
     * new FiltersAggregator.KeyedFilter("men", QueryBuilders.termQuery("gender", "male"))
     *
     * @param metricName
     * @param aggsType
     * @param clazz
     * @param queryBuilder
     * @param filters
     * @return
     * @throws Exception
     */
    public Map filterAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, FiltersAggregator.KeyedFilter... filters) throws Exception;

    /**
     * 直方图聚合
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param interval
     * @return
     * @throws Exception
     */
    public Map histogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, double interval) throws Exception;


    /**
     * 日期直方图聚合
     *
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param interval
     * @return
     * @throws Exception
     */
    public Map dateHistogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, DateHistogramInterval interval) throws Exception;

}
