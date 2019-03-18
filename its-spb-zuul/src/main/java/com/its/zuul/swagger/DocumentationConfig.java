package com.its.zuul.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * Swagger API接口文档整合<br>
 * 通过访问http://127.0.0.1:80/swagger-ui.html查看API
 * 
 * @author tzz
 */
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    public static final String API_URI = "/v2/api-docs";
    private final RouteLocator routeLocator;

    public DocumentationConfig(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        // resources.add(swaggerResource("its-spb-base-servers", "/api-base" + API_URI, "2.0"));
        // resources.add(swaggerResource("its-spb-order-servers", "/api-order" + API_URI, "2.0"));
        List<Route> routes = routeLocator.getRoutes();
        routes.stream().filter(route -> !route.getId().isEmpty() && !"unknown".equals(route.getId())).forEach(route -> {
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", API_URI), "2.0"));
        });
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
