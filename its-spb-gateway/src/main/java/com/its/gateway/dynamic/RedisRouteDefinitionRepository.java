package com.its.gateway.dynamic;

import static java.util.Collections.synchronizedMap;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.its.gateway.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 集群环境动态路由配置--通过自定义实现RouteDefinitionRepository接口从数据库、配置中心、Redis获取路由进行<br>
 * 当前自定义实现是使用Redis保存自定义路由配置（代替默认内存的实现方式InMemoryRouteDefinitionRepository）
 *
 * @author tzz
 * @date 2019/03/04
 */

@Component
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    private RedisRouteService redisRouteService;
    private StringRedisTemplate redisTemplate;

    @Autowired
    public RedisRouteDefinitionRepository(RedisRouteService redisRouteService, StringRedisTemplate redisTemplate) {
        this.redisRouteService = redisRouteService;
        this.redisTemplate = redisTemplate;
    }

    public final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<>());

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            // 网关路由代码例子
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.setId("its-spb-base-servers");
            List<PredicateDefinition> predicates = new ArrayList<>();
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            predicateDefinition.setName("Path");
            Map<String, String> args = new HashMap<>(16);
            args.put("_genkey_0", "/api-base2/**");
            predicateDefinition.setArgs(args);
            predicates.add(predicateDefinition);
            routeDefinition.setPredicates(predicates);

            List<FilterDefinition> filters = new ArrayList<>();
            FilterDefinition filterDefinition1 = new FilterDefinition();
            filterDefinition1.setName("StripPrefix");
            Map<String, String> fArgs1 = new HashMap<>(16);
            fArgs1.put("_genkey_0", "1");
            filterDefinition1.setArgs(fArgs1);
            filters.add(filterDefinition1);
            FilterDefinition filterDefinition2 = new FilterDefinition();
            filterDefinition2.setName("RequestTime");
            Map<String, String> fArgs2 = new HashMap<>(16);
            fArgs2.put("_genkey_0", "true");
            filterDefinition2.setArgs(fArgs2);
            filters.add(filterDefinition2);
            routeDefinition.setFilters(filters);
            URI uri;
            uri = new URI("lb://its-spb-base-servers");
            routeDefinition.setUri(uri);
            routeDefinition.setOrder(0);
            String json = JSON.toJSONString(routeDefinition);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 网关路由JSON例子
        String itsSpbBaseServers =
            "{'id':'its-spb-base-servers','order':0,'uri':'lb://its-spb-base-servers','predicates':[{'args':{'_genkey_0':'/api-base/**'},'name':'Path'}],"
                + "'filters':[{'args':{'_genkey_0':'1'},'name':'StripPrefix'},{'args':{'_genkey_0':'true'},'name':'RequestTime'}]}";
        String itsSpbOrderServers =
            "{'id':'its-spb-order-servers','order':0,'uri':'lb://its-spb-order-servers','predicates':[{'args':{'_genkey_0':'/api-order/**'},'name':'Path'}],"
                + "'filters':[{'args':{'_genkey_0':'1'},'name':'StripPrefix'},{'args':{'_genkey_0':'true'},'name':'RequestTime'}]}";
        System.out.println(itsSpbBaseServers);
        System.out.println(itsSpbOrderServers);

        // 获取服务网关动态路由(Redis/数据库)
        List<RouteDefinition> routeDefinitions = redisRouteService.getRouteDefinitions();
        // 设置服务网关动态路由
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition -> {
            redisTemplate.opsForHash().put(RedisUtil.GATEWAY_ROUTES, routeDefinition.getId(), JSON.toJSONString(routeDefinition));
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (redisTemplate.opsForHash().hasKey(RedisUtil.GATEWAY_ROUTES, id)) {
                redisTemplate.opsForHash().delete(RedisUtil.GATEWAY_ROUTES, id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }

}
