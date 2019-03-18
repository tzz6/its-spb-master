package com.its.gateway.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.its.gateway.dynamic.RedisRouteService;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * Gateway整合Swagger
 * 
 * @author tzz
 */
@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {
    public static final String API_URI = "/v2/api-docs";
    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;
    @Autowired
    private RedisRouteService redisRouteService;

    public SwaggerProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        this.routeLocator = routeLocator;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        // 取出Spring Cloud Gateway中的route
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        // 获取application.yml中的路由配置，只获取有效的route节点
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
            .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName())).forEach(
                    predicateDefinition -> resources.add(swaggerResource(routeDefinition.getId(), predicateDefinition
                        .getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", API_URI), "2.0"))));

        // 获取动态路由配置（Redis/数据库）
        
        List<RouteDefinition> routeDefinitions = redisRouteService.getRouteDefinitions();
        routeDefinitions.forEach(routeDefinition -> {
            routeDefinition.getPredicates().stream()
                .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                .forEach(predicateDefinition -> resources.add(swaggerResource(routeDefinition.getId(),
                    predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", API_URI),
                    "2.0")));
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
