package com.its.base.servers.es.rhl;

import com.its.base.servers.BaseTest;
import com.its.base.servers.es.rhl.domain.EsSysUser;
import com.its.common.es.rhl.enums.AggsType;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description:ElasticSearchTest
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2019/12/18 10:51
 */
public class ElasticsearchBaseImplTest extends BaseTest {
    @Autowired
    private ElasticSearchBase elasticSearchBase;
    @Autowired
    private ElasticSearchDml elasticSearchDml;

    /**
     * 创建索引
     */
    @Test
    public void testCreateIndex() {
        try {
            //索引是否存在
            boolean isExists = elasticSearchBase.exists(EsSysUser.class);
            if (isExists) {
                //存在则删除再创建
                elasticSearchBase.delete(EsSysUser.class);
            }
            elasticSearchBase.createIndex(EsSysUser.class);
            testBulkSave();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增索引数据
     */
    @Test
    public void testSave() {
        EsSysUser esSysUser = new EsSysUser("1", "0115", "TZZ", "123456", 100L, "CN",
                "SYS", "ABC", new Date(), new Date());
        boolean flag = elasticSearchDml.save(esSysUser);
    }

    /**
     * 批量新增索引数据
     */
    @Test
    public void testBulkSave() {
        List<EsSysUser> esSysUserList = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            String stName = "TEST";
            String language = "English";
            Long age = 100L;
            if (i % 2 == 0) {
                stName = "测试";
                age = 50L;
            }
            if (i % 2 == 0) {
                language = "中华人民共和国";
                age = 70L;
            }
            if (i % 3 == 0) {
                language = "香港";
                age = 30L;
            }
            if (i % 5 == 0) {
                language = "aomen";
                age = 20L;
            }
            EsSysUser esSysUser = new EsSysUser(i + "", "0115" + i, stName + i, "123456", age, language,
                    "SYS", "ABC", new Date(), new Date());
            esSysUserList.add(esSysUser);
        }
        EsSysUser esSysUser = new EsSysUser("1", "0115", "TZZ", "123456", 100L, "CN",
                "SYS", "ABC", new Date(), new Date());
        BulkResponse bulkResponse = elasticSearchDml.bulkSave(esSysUserList);
        bulkResponse.forEach(response -> {
            System.out.println(response.getResponse().getResult());
        });
    }

    /**
     * 按照有值字段更新索引
     */
    @Test
    public void testUpdate() {
        EsSysUser esSysUser = new EsSysUser("3", "0115", "TanZZ02");
        elasticSearchDml.update(esSysUser);
    }

    /**
     * 按照有值字段更新索引
     */
    @Test
    public void testDelete() {
        EsSysUser esSysUser = new EsSysUser();
        esSysUser.setStId("3");
        boolean flag = elasticSearchDml.delete(esSysUser);
        flag = elasticSearchDml.deleteById("4", EsSysUser.class);
    }

    @Test
    public void testGetById() {
        //根据ID查询
        EsSysUser esSysUser = (EsSysUser) elasticSearchDml.getById("6", EsSysUser.class);
        System.out.println(esSysUser.toString());
        //根据ID列表批量查询
        String[] ids = new String[]{"7", "8"};
        List<EsSysUser> esSysUserList = elasticSearchDml.mgetById(ids, EsSysUser.class);
        esSysUserList.forEach(esUser -> {
            System.out.println(esUser.toString());
        });
        //id数据是否存在
        System.out.println(elasticSearchDml.exists("6", EsSysUser.class));
    }

    @Test
    public void testSearch() {
        SearchRequest searchRequest = new SearchRequest();
        //searchRequest.indices("es_sys_user");
        searchRequest.indices("test", "es_sys_user");//可指定多个索引库
        //searchRequest.indices("es_sys_*");//索引可使用通配符
        //searchRequest.types("sys_user");
        searchRequest.types("sys_user", "user");//可指定多个type

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.query(QueryBuilders.matchAllQuery());//查询全部
        //searchSourceBuilder.query(QueryBuilders.termQuery("stId", "1"));
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("TEST1", "name", "stName"));//多字段查询
        searchSourceBuilder.from(0);//起始 index
        searchSourceBuilder.size(100);//大小 size
        searchRequest.source(searchSourceBuilder);
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        SearchResponse searchResponse = elasticSearchDml.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        List<EsSysUser> list = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            System.out.println(hit.getSourceAsString());
//            EsSysUser user = JacksonUtil.nonDefaultMapper().fromJson(hit.getSourceAsString(), EsSysUser.class);
//            list.add(user);
        }
        list.forEach(esUser -> {
            System.out.println(esUser.toString());
        });
    }

    @Test
    public void testSearch2() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder
                //.query(QueryBuilders.matchAllQuery())//查询全部
                //.query(QueryBuilders.matchQuery("language", "中国香港"))//match搜索会先对搜索词进行分词，对于最基本的match搜索，只要搜索词的分词集合中的一个或多个存在于文档中即可
                //.query(QueryBuilders.multiMatchQuery("测试26", "stCode", "stName"))//多字段查询
                //.query(QueryBuilders.termQuery("stId", "1"))//精确查找,结构化字段查询，匹配一个值，且输入的值不会被分词器分词
                //.query(QueryBuilders.termsQuery("stId", "1", "2"))//精确查找,一次匹配多个值
                .query(QueryBuilders.queryStringQuery("中国香TEST").field("language").defaultOperator(Operator.OR))//模糊查询
                .postFilter(QueryBuilders.rangeQuery("stId").from(5).to(32))//区间查询
                .from(0)//起始 index
                .size(50)//大小 size
                .sort(new FieldSortBuilder("stId").order(SortOrder.ASC))//排序
                .timeout(new TimeValue(60, TimeUnit.SECONDS));// 设置搜索的超时时间
        List<EsSysUser> esSysUserList = elasticSearchDml.search(sourceBuilder, SearchType.DFS_QUERY_THEN_FETCH, EsSysUser.class);
        esSysUserList.forEach(esUser -> {
            System.out.println(esUser.toString());
        });
    }

    /**
     * 组合查询
     */
    @Test
    public void testSearchMust() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("stId", "1"))// AND
                .mustNot(QueryBuilders.termQuery("stId", "2"))//NOT
                .should(QueryBuilders.termQuery("stId", "3")));//OR
        List<EsSysUser> esSysUserList = elasticSearchDml.search(sourceBuilder, SearchType.DFS_QUERY_THEN_FETCH, EsSysUser.class);
        esSysUserList.forEach(esUser -> {
            System.out.println(esUser.toString());
        });
    }

    /**
     * 聚合查询
     */
    @Test
    public void testSearchAggregations() {
        // 按国家地址分组聚合统计select language,count(st_id) as by_language from sys_user group by language
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_age").field("age");
        Aggregations aggregations = elasticSearchDml.searchAggregations(aggregation, QueryBuilders.matchAllQuery(), EsSysUser.class);
        // 获取分组信息
        Terms terms = aggregations.get("by_age");
        for (Terms.Bucket entry : terms.getBuckets()) {
            Object key = entry.getKey();
            long docCount = entry.getDocCount();
            System.out.println(key + "@" + docCount);
        }

        Map<Object, Object> maps = elasticSearchDml.searchAggregations("stId", "age", AggsType.avg, QueryBuilders.matchAllQuery(), EsSysUser.class);
        maps.forEach((k, v) -> {
            System.out.println("Item : " + k + " Count : " + v);
        });
    }

}
