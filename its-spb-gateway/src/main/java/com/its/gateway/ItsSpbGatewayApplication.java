package com.its.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
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

import com.its.gateway.filter.RequestTimeGatewayFilterFactory;

import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
  * Description: Gatewey网关 @MapperScan指定要扫描的Mapper类的包的路径
  * Company: tzz
  * @Author: tzz
  * Date: 2020/4/17 23:00
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

    /** headers */
    private static final String ALLOWED_HEADERS =
        "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,its-username,its-language,its-refreshToken,its-token,client";
    private static final String ALLOWED_METHODS = "*";
    /** #允许跨域请求的域名或服务器IP,“*”为允许所有，请求带自定义Headers，不允许使用“*”通配符，需要指定相应域名或服务器IP */
    private static final String ALLOWED_ORIGIN = "http://localhost:8080,http://its.com:82,http://spb.com:82,http://cas.com:8080";
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
                String[] multiOriginArray = ALLOWED_ORIGIN.split(",");
                Set<String> allowedOrigins = new HashSet<>(Arrays.asList(multiOriginArray));
                String originHeads = ctx.getRequest().getHeaders().getFirst("Origin");
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                if (originHeads != null && allowedOrigins.contains(originHeads)) {
                    headers.add("Access-Control-Allow-Origin", originHeads);
                }
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

   /**
    *
    * description: CORS解决前端调用跨域问题(可使用application-cors.yml配置代替)-设置Headers时用上述WebFilter代替
    * @author: tzz
    * date: 2019/08/22 10:56
    * @return CorsWebFilter

    @Bean
    public CorsWebFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //在生产环境上最好指定域名，以免产生跨域安全问题
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfiguration.addAllowedHeader(ALLOWED_HEADERS);
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }*/

}
