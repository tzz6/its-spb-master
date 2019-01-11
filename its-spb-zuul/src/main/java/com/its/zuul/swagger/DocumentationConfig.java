package com.its.zuul.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * Swagger API接口文档整合<br>
 * 通过访问http://127.0.0.1:80/swagger-ui.html查看API
 *
 */
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {
	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		resources.add(swaggerResource("its-spb-base-servers", "/api-base/v2/api-docs", "2.0"));
		resources.add(swaggerResource("its-spb-order-servers", "/api-order/v2/api-docs", "2.0"));
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location, String version) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion(version);
		return swaggerResource;
	}
}
