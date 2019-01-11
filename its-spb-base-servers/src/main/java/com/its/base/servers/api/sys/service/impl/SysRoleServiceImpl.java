package com.its.base.servers.api.sys.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.base.servers.api.sys.service.SysRoleService;
import com.its.common.model.ResponseBase;

@RestController
public class SysRoleServiceImpl implements SysRoleService {

	@Value("${server.port}")
	private String port;

	@GetMapping("/getSysRoleById")
	@Override
	public String getSysRoleById(String id) {
		Map<String, String> maps = new HashMap<>();
		for (int i = 0; i < 100; i++) {
			maps.put(i + "", "test" + i);
		}
		String value = maps.get(id);
		if (value == null) {
			throw new RuntimeException("该ID:" + id + "没有对应的信息");
		}
		return "Order服务接口调用Base服务--getSysRoleById--" + value + "--" + port;
	}

	@PostMapping("/getSysRole")
	@Override
	public ResponseBase getSysRole() {
		try {
			// 设置业务处理时间为1.5S
			Thread.sleep(1500);
			System.out.println("getSysRole");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseBase(200, "Order服务接口调用Base服务接口成功......getSysRole", null);
	}

}
