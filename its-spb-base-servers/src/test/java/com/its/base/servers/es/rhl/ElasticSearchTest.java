package com.its.base.servers.es.rhl;

import com.its.base.servers.BaseTest;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Description:ElasticSearchTest
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2019/12/18 10:51
 */
public class ElasticSearchTest extends BaseTest {
    private RestHighLevelClient highLevelClient;

    /**
     * 使用Java RestHighLevelClient 连接ElasticSearch 集群
     */
    @Before
    public void initClient() {
        highLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("vm-es-ip", 9200, "http")));
    }

    /**
     * 查看索引是否存在
     */
    @Test
    public void exists() {
        GetIndexRequest request = new GetIndexRequest();
        String index = "test";
        request.indices(index);
        boolean exists = false;
        try {
            exists = highLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(exists){
            System.out.println("test索引库存在");
        }else{
            System.out.println("test索引库不存在");
        }

    }
}
