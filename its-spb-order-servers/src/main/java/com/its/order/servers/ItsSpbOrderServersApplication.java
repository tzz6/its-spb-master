package com.its.order.servers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * its-spb-order-servers服务启动
 *
 */
@SpringBootApplication

// @EnableEurekaClient 将当前服务注册到Eureka注册中心,如果注册中心是Eureka使用，向注册中心注册服务
@EnableEurekaClient
// @EnableDiscoveryClient如果注册中心是Zookeeper、Connsul使用，向注册中心注册服务
// @EnableDiscoveryClient
// @EnableFeignClients注解开启Feign功能
@EnableFeignClients
//@EnableHystrix注解开启Hystrix功能
@EnableHystrix
public class ItsSpbOrderServersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItsSpbOrderServersApplication.class, args);
	}

	// 将RestTemplate注册到SpringBoot容器中，方便其他类使用@Autowired自动注入RestTemplate
	// 如果使用rest以别名方式进行调用依赖Ribbon负载均衡器@LoadBalanced
	// @LoadBalanced就能让这个RestTemplate在请求时拥有客服端负载均衡能力
	@Bean
	@LoadBalanced
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
