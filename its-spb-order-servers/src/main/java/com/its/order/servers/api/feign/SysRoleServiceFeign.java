package com.its.order.servers.api.feign;

import org.springframework.cloud.openfeign.FeignClient;

import com.its.base.servers.api.sys.service.SysRoleService;
import com.its.order.servers.api.fallback.SysRoleServiceFallback;

/**
 * 方式二：@FeignClient中的fallback类级别实现，需要在yml中配置开启Feign的Hystrix熔断器(研发中主要使用这种方式)
 * 
 * @author tzz
 */
@FeignClient(name = "its-spb-base-servers", fallback = SysRoleServiceFallback.class)
public interface SysRoleServiceFeign extends SysRoleService {

}
