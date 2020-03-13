package com.its.gateway.filter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 
 * description: IP白名单
 * company: tzz
 * @author: tzz
 * date: 2019/08/17 14:53
 */
@Component
public class IpBlackListFilter implements GlobalFilter, Ordered {

    private final Logger log = LoggerFactory.getLogger(IpBlackListFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            // IP白名单
            String [] ipWhitelist = new String[] {"localhost", "127.0.0.1", "10.118.14.16"};
            String ip = getIpAddr(exchange);
            if (checkFilter(ip, ipWhitelist)) {
                log.info("intercept invalid request from forbidden ip {}", ip);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return Mono.empty();
            }
        } catch (Exception e) {
            log.error("IpBlackListFilter error", e);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    /** 检查是否是要过滤 */
    private boolean checkFilter(String value, String[] notFilter) {
        for (String str : notFilter) {
            if (str.equals(value)) {
                return false;
            }
        }
        return true;
    }

    private static String getIpAddr(ServerWebExchange exchange) {
        String ip = null;
        String localHost = "127.0.0.1";
        String local = "0:0:0:0:0:0:0:1";
        try {
            InetSocketAddress inetSocketAddress = exchange.getRequest().getHeaders().getHost();
            if (inetSocketAddress != null) {
                ip = inetSocketAddress.getHostName();
            }
            if (localHost.equals(ip) || local.equals(ip)) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
