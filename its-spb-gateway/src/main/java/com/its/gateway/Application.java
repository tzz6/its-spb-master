package com.its.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
/**
 * Gatewey网关简单例子
 *
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	//创建一个简单路由RouteLocator的Bean进行路由转发，将请求进行处理，最后转发到目标的下游服务。
	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes().route(
				p -> p.path("/get").filters(f -> f.addRequestHeader("Gateway", "HelloWorld")).uri("http://httpbin.org:80"))
				.build();
	}
}
