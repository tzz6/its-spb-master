package com.its.base.servers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.spring4all.swagger.EnableSwagger2Doc;

/**
 * its-spb-base-servers服务启动
 *
 */
@SpringBootApplication
// @EnableEurekaClient 将当前服务注册到Eureka注册中心,如果注册中心是Eureka使用，向注册中心注册服务
@EnableEurekaClient
// @EnableDiscoveryClient如果注册中心是Zookeeper、Connsul使用，向注册中心注册服务
// @EnableDiscoveryClient
// @EnableSwagger2Doc开启Swagger
@EnableSwagger2Doc
public class ItsSpbBaseServersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItsSpbBaseServersApplication.class, args);
	}
}