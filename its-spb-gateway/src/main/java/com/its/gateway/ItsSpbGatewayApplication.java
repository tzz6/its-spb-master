package com.its.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.its.gateway.filter.RequestTimeGatewayFilterFactory;

/**
 * Gatewey网关
 * 
 * @MapperScan指定要扫描的Mapper类的包的路径
 * @author tzz
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.its.gateway.mapper"})
public class ItsSpbGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsSpbGatewayApplication.class, args);
    }

    @Bean
    public RequestTimeGatewayFilterFactory requestTimeGatewayFilterFactory() {
        return new RequestTimeGatewayFilterFactory();
    }
}
