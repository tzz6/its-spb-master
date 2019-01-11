package com.its.base.servers.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("会员接口服务")
public class MemberApiController {

	@Value("${server.port}")
	private String port;

	@ApiOperation("获取会员信息")
	@GetMapping("/getMember")
	public String getMember() {
		return "this is member,这是会员服务" + port;
	}
}
