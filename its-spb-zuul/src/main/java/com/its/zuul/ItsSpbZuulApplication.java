package com.its.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;

import com.its.zuul.filter.ItsServerFilter;
import com.spring4all.swagger.EnableSwagger2Doc;

/**
 * its-spb-zuul服务启动<br>
 * Zuul集群:可采用Nginx+zuul 一主一备或多机轮询<br>
 * 客户端发送请求统一到Nginx,再使用Nginx(LVS+keepalived)实现反向代理和负载均衡，采用轮询算法转发到zuul网关<br>
 * 
 * @EnableEurekaClient 将当前服务注册到Eureka注册中心,如果注册中心是Eureka使用，向注册中心注册服务<br>
 * @EnableDiscoveryClient如果注册中心是Zookeeper、Connsul使用@EnableDiscoveryClient替换@EnableEurekaClient，向注册中心注册服务<br>
 * @EnableZuulProxy 开启Zuul网关代理<br>
 * @EnableSwagger2Doc开启Swagger<br>
 * 
 * @author tzz
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@EnableSwagger2Doc
public class ItsSpbZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItsSpbZuulApplication.class, args);
	}

	@Bean
	public ItsServerFilter itsServerFilter() {
		return new ItsServerFilter();
	}

	/**
	 * 通过将路由配置放到配置中心实现动态路由
	 * 
	 * @return
	 */
	@RefreshScope
	@ConfigurationProperties("zuul")
	public ZuulProperties zuulProperties() {
		return new ZuulProperties();
	}
}
