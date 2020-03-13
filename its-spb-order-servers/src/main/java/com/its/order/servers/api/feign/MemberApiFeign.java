package com.its.order.servers.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
/**
 *
 * @author tzz
 * @date 2019/03/18
 * @Introduce: MemberApiFeign
 */
@FeignClient(name = "its-spb-base-servers")
public interface MemberApiFeign {

    /**
     * getMember
     * @return
     */
	@GetMapping("/getMember")
	String getMember();
}
