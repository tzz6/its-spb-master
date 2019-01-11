package com.its.base.servers.api.sys.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.its.common.model.ResponseBase;

public interface SysRoleService {

	@GetMapping("/getSysRoleById")
	public String getSysRoleById(@RequestParam(value = "id") String id);

	@PostMapping("/getSysRole")
	public ResponseBase getSysRole();

}
