package com.its.order.servers.api.feign;

import org.springframework.cloud.openfeign.FeignClient;

import com.its.base.servers.api.sys.service.SysUserService;

@FeignClient(name = "its-spb-base-servers")
public interface SysUserServiceFeign extends SysUserService{

}
