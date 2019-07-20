package com.its.gateway.dynamic;

import static java.util.Collections.synchronizedMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.its.gateway.domain.GatewayRouter;
import com.its.gateway.mapper.GatewayRouteMapper;

/**
 * 集群环境动态路由配置-获取动态路由配置(Redis/数据库)
 * 
 * @author tzz
 */

@Component
public class RedisRouteService {
    private static final Logger log = LoggerFactory.getLogger(RedisRouteService.class);
    public static final String GATEWAY_ROUTES = "its_geteway_routes";
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;

    /** 为优化redis动态路由网关配置性能，可将动态配置保存至内存，通过消息总线通知各节点刷新 */
    public final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<String, RouteDefinition>());

    public List<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        try {
            // 删除---用于测试，真实环境去掉redis删除代码
            gatewayRouteMapper.deleteGatewayRouteAll();
            // 新增
            GatewayRouter baseGatewayRouter = new GatewayRouter();
            baseGatewayRouter.setKey("its-spb-base-servers");
            baseGatewayRouter.setGatewayRoute(
                "{'id':'its-spb-base-servers','order':0,'uri':'lb://its-spb-base-servers','predicates':[{'args':{'_genkey_0':'/api-base/**'},'name':'Path'}],"
                    + "'filters':[{'args':{'_genkey_0':'1'},'name':'StripPrefix'},{'args':{'_genkey_0':'true'},'name':'RequestTime'}]}");
            baseGatewayRouter.setStatus("0");
            GatewayRouter orderGatewayRouter = new GatewayRouter();
            orderGatewayRouter.setKey("its-spb-order-servers");
            orderGatewayRouter.setGatewayRoute(
                "{'id':'its-spb-order-servers','order':0,'uri':'lb://its-spb-order-servers','predicates':[{'args':{'_genkey_0':'/api-order/**'},'name':'Path'}],"
                    + "'filters':[{'args':{'_genkey_0':'1'},'name':'StripPrefix'},{'args':{'_genkey_0':'true'},'name':'RequestTime'}]}");
            orderGatewayRouter.setStatus("0");
            gatewayRouteMapper.insertGatewayRoute(baseGatewayRouter);
            gatewayRouteMapper.insertGatewayRoute(orderGatewayRouter);
            // 修改
            baseGatewayRouter.setStatus("1");
            gatewayRouteMapper.updateGatewayRouteByKey(baseGatewayRouter);
            orderGatewayRouter.setStatus("1");
            gatewayRouteMapper.updateGatewayRouteByKey(orderGatewayRouter);

            List<Object> objects = redisTemplate.opsForHash().values(GATEWAY_ROUTES);
            // Redis中无动态路由配置
            if (objects == null || objects.size() == 0) {
                log.info("将数据从数据库加载至Redis");
                // 数据库获取动态路由配置
                List<GatewayRouter> gatewayRouters = gatewayRouteMapper.getGatewayRouteList();
                gatewayRouters.forEach(gatewayRouter -> {
                    RouteDefinition routeDefinition =
                        JSON.parseObject(gatewayRouter.getGatewayRoute(), RouteDefinition.class);
                    // 保存至Redis
                    redisTemplate.opsForHash().put(GATEWAY_ROUTES, routeDefinition.getId(),
                        JSON.toJSONString(routeDefinition));
                });
            } else {
                log.info("从Redis获取数据");
            }

            redisTemplate.opsForHash().values(GATEWAY_ROUTES).stream().forEach(route -> {
                String routeDefinitionJson = route.toString();
                log.info(routeDefinitionJson);
                RouteDefinition routeDefinition = JSON.parseObject(routeDefinitionJson, RouteDefinition.class);
                String id = routeDefinition.getId();
                if (routes.get(id) == null) {
                    routes.put(id, routeDefinition);
                }
                // 添加路由设置
                routeDefinitions.add(routeDefinition);
                // redis删除---用于测试，真实环境去掉redis删除代码
                redisTemplate.opsForHash().delete(GATEWAY_ROUTES, routeDefinition.getId());
            });
        } catch (Exception e) {
            log.info("message:" + e);
        }
        return routeDefinitions;
    }

}