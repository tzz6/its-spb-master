package com.its.config.servers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * SpringConfig配置中心服务端
 *
 * 注解EnableEurekaClient 将当前服务注册到Eureka注册中心,如果注册中心是Eureka使用，向注册中心注册服务<br>
 * 注解@EnableConfigServer注解开启ConfigServer功能
 * @author tzz
 */
@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class ItsSpbConfigServersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsSpbConfigServersApplication.class, args);
    }
}
