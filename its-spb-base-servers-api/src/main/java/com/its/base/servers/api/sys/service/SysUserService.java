package com.its.base.servers.api.sys.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.its.base.servers.api.sys.domain.SysUser;
import com.its.common.model.ResponseBase;

public interface SysUserService {

	@GetMapping("/getSysUserByStId")
	public SysUser getSysUserByStId(@RequestParam(value = "stId") String stId);

	@PostMapping("/getSysUser")
	public ResponseBase getSysUser(@RequestBody SysUser sysUser);

}
