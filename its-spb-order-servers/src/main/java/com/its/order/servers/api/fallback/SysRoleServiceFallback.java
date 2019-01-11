package com.its.order.servers.api.fallback;

import org.springframework.stereotype.Component;

import com.its.common.model.ResponseBase;
import com.its.common.utils.DateUtil;
import com.its.order.servers.api.feign.SysRoleServiceFeign;

/**
 * 方式二：@FeignClient中的fallback类级别实现，需要在yml中配置开启Feign的Hystrix熔断器(研发中主要使用这种方式)
 *
 */
@Component
public class SysRoleServiceFallback implements SysRoleServiceFeign {

	@Override
	public String getSysRoleById(String id) {
		String result = "服务降级---该ID:" + id + "没有对应的信息--(方式二：@FeignClient中的fallback类级别实现，需要在yml中配置开启Feign的Hystrix熔断器)"
				+ Thread.currentThread().getName() + "--" + DateUtil.getDateyyyyMMddHHmmss();
		return result;
	}

	@Override
	public ResponseBase getSysRole() {
		String result = "服务降级，友好提示，服务器忙请稍后重试(方式二：@FeignClient中的fallback类级别实现，需要在yml中配置开启Feign的Hystrix熔断器)"
				+ Thread.currentThread().getName() + "--" + DateUtil.getDateyyyyMMddHHmmss();
		System.out.println(result);
		return new ResponseBase(200, result, null);
	}

}
