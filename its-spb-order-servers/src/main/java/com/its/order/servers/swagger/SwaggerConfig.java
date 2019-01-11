package com.its.order.servers.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * Swagger2配置
 *
 */
@Configuration
// @EnableSwagger2开启Swagger
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				// 生成API扫包范围
				.apis(RequestHandlerSelectors.basePackage("com.its.order.servers.api")).paths(PathSelectors.any()).build();
	}

	/**
	 * 创建API文档信息
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("SpringBoot|SpringCloud学习").description("SpringBoot|SpringCloud学习")
				.termsOfServiceUrl("https://its.com")
				.contact("ITS-TZZ")
				.version("1.0.0").build();

	}
}
