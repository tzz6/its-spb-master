package com.its.order.servers.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "its-spb-base-servers")
public interface MemberApiFeign {

	@GetMapping("/getMember")
	public String getMember();
}
