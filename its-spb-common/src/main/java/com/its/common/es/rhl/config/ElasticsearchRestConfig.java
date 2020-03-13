package com.its.common.es.rhl.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:ES客户端配置
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2019/12/18 11:02
 */
public class ElasticsearchRestConfig {
    /**
     * ES服务器地址ip:port
     */
    @Value("${elasticsearch.ip}")
    String ipPort;

    @Bean
    public RestClientBuilder restClientBuilder() {
        return RestClient.builder(makeHttpHost(ipPort));
    }

    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        restClientBuilder.setMaxRetryTimeoutMillis(60000);
        return new RestHighLevelClient(restClientBuilder);
    }

    private HttpHost makeHttpHost(String ipPort) {
        if (ipPort != null && !"".equals(ipPort)) {
            String[] address = ipPort.split(":");
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, "http");
        } else {
            return null;
        }
    }
}
