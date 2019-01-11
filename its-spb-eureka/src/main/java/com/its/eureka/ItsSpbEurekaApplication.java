package com.its.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * SpringBoot启动
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class ItsSpbEurekaApplication {

	// @EnableEurekaServer 表示开启EurekaServer服务 开启注册中心
	public static void main(String[] args) {
		SpringApplication.run(ItsSpbEurekaApplication.class, args);
	}
	// SpringCloud支持三种注册中心:Eureka/Zookeeper/Consul
}