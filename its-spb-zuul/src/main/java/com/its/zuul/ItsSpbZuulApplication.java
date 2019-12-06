package com.its.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.its.zuul.filter.ItsServerFilter;
import com.spring4all.swagger.EnableSwagger2Doc;

import reactor.core.publisher.Mono;

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
	
    /** headers */
    private static final String ALLOWED_HEADERS =
        "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,its-username,its-language,its-refreshToken,its-token,client";
    private static final String ALLOWED_METHODS = "*";
    /** #允许跨域请求的域名或服务器IP,多个用逗号分隔，“*”为允许所有，请求带自定义Headers，不允许使用“*”通配符，需要指定相应域名或服务器IP */
    private static final String ALLOWED_ORIGIN = "http://localhost:8080";
    private static final String ALLOWED_EXPOSE =
        "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,its-username,its-language,its-refreshToken,its-token,client";
    private static final String MAX_AGE = "18000";

    /**
     * 
     * description: CORS解决前端调用跨域问题--请求带自定义Headers
     * @author: tzz
     * date: 2019/08/22 10:34
     * @return WebFilter
     */
    @Bean
    public WebFilter webCorsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Max-Age", MAX_AGE);
                headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                headers.add("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
                headers.add("Access-Control-Allow-Credentials", "true");
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }
}
