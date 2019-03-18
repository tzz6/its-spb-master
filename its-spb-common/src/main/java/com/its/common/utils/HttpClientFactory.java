package com.its.common.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 
 * @author tzz
 * @date 2019/02/22
 * @Introduce: Write describe here
 */
public class HttpClientFactory {

    /** 连接池最大连接数 */
    private static final Integer MAX_TOTAL = 300;
    /** 单个路由默认最大连接数 */
    private static final Integer MAX_PER_ROUTE = 50;
    /** 请求超时时间ms */
    private static final Integer REQ_TIMEOUT = 5 * 1000;
    /** 连接超时时间ms */
    private static final Integer CONN_TIMEOUT = 5 * 1000;
    /** 读取超时时间ms */
    private static final Integer SOCK_TIMEOUT = 10 * 1000;
    /** HTTP链接管理器线程 */
    private static HttpClientConnectionMonitorThread thread;

    public static HttpClientConnectionMonitorThread getThread() {
        return thread;
    }

    public static void setThread(HttpClientConnectionMonitorThread thread) {
        HttpClientFactory.thread = thread;
    }

    public static HttpClient createSimpleHttpClient() {
        SSLConnectionSocketFactory sf = SSLConnectionSocketFactory.getSocketFactory();
        return HttpClientBuilder.create().setSSLSocketFactory(sf).build();
    }

    public static HttpClient createHttpClient() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
            new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(REQ_TIMEOUT)
            .setConnectTimeout(CONN_TIMEOUT).setSocketTimeout(SOCK_TIMEOUT).build();
        // 管理
        // http连接池
        HttpClientFactory.thread = new HttpClientConnectionMonitorThread(poolingHttpClientConnectionManager); 
        return HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
            .setDefaultRequestConfig(requestConfig).build();
    }
}
