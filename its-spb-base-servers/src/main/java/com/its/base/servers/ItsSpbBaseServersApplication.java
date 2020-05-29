package com.its.base.servers;

import com.its.common.es.rhl.config.ElasticsearchRestConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.context.annotation.Import;

/**
 * its-spb-base-servers服务启动
 *
 * @EnableEurekaClient 将当前服务注册到Eureka注册中心,如果注册中心是Eureka使用，向注册中心注册服务<br>
 * @EnableDiscoveryClient如果注册中心是Zookeeper、Connsul使用@EnableDiscoveryClient替换@EnableEurekaClient，向注册中心注册服务<br>
 * @EnableSwagger2Doc开启Swagger<br>
 * @Import自定义Config未使用@Configuration注解，则需要Import引入<br>
 * 使用@Configuration注解需要与SpringBootApplication在同一package下，不在同一package下则需要使用scanBasePackages = "com.its"<br>
 * @author tzz
 */
@SpringBootApplication(scanBasePackages = {"com.its.base","com.its.common.redis"})
@EnableEurekaClient
@EnableSwagger2Doc
@MapperScan("com.its.base.servers.mapper")
@Import(value = {ElasticsearchRestConfig.class})
public class ItsSpbBaseServersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsSpbBaseServersApplication.class, args);
    }
}
