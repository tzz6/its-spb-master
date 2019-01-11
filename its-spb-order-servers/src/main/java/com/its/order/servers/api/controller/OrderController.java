package com.its.order.servers.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.its.order.servers.api.feign.MemberApiFeign;

@RestController
public class OrderController {

	// 在SpringCloud中支持两种客户端调用工具
	// 方式一：RestTemplate,RestTemplate是由SpringBoot Web组件提供 默认整合Ribbon负载均衡器
	// rest方式底层采用httpclient技术实现
	@Autowired
	private RestTemplate restTemplate;

	// 方式二：Feign(实际开发中主要使用Feigin,RestTemplate基本不使用)
	// Feign是一个声明式的Http客户端调用工具，采用接口+注解方式实现，易读性比较强
	@Autowired
	private MemberApiFeign memberApiFeign;
	//获取注册中心的注册服务列表
	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping("/getOrder")
	public String getOrder() {
		List<ServiceInstance> serviceInstances = discoveryClient.getInstances("its-spb-base-servers");
		for (ServiceInstance serviceInstance : serviceInstances) {
			System.out.println(serviceInstance.getUri());
		}
		return "this is order,这是订单服务---its-spb-base-servers注册服务列表数：" + serviceInstances.size();
	}

	/** 方式一：RestTemplate方式客户端调用 ---Order服务调用Base服务 */
	@GetMapping("/restOrderToBase")
	public String getOrderToBase() {
		// 两种方式调用
		// 方式一：使用服务别名调用
		String url = "http://its-spb-base-servers/getMember";
		// 方式二：直接调用，使用服务IP+端口的方式调用
		// String url = "http://127.0.0.1:8001/getMember";
		String result = restTemplate.getForObject(url, String.class);
		result = "RestTemplate方式客户端调用 ---Order服务调用Base服务 result:" + result;
		System.out.println(result);
		return result;
	}

	/** 方式二：Feign方式客户端调用 ---Order服务调用Base服务 */
	@GetMapping("/feignOrderToBase")
	public String feignOrderToBase() {
		String result = memberApiFeign.getMember();
		result = "Feign方式客户端调用 ---Order服务调用Base服务result:" + result;
		System.out.println(result);
		return result;
	}
}