package com.its.base.servers.api.sys.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.base.servers.api.sys.domain.SysUser;
import com.its.base.servers.api.sys.service.SysUserService;
import com.its.common.model.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("SysUser服务接口")
@RestController
public class SysUserServiceImpl implements SysUserService {

	@Value("${server.port}")
	private String port;

	// @Api：修饰整个类，描述Controller的作用
	// @ApiOperation：描述一个类的一个方法，或者说一个接口
	// @ApiParam：单个参数描述
	// @ApiModel：用对象来接收参数
	// @ApiProperty：用对象接收参数时，描述对象的一个字段
	// @ApiResponse：HTTP响应其中1个描述
	// @ApiResponses：HTTP响应整体描述
	// @ApiIgnore：使用该注解忽略这个API
	// @ApiError ：发生错误返回的信息
	// @ApiImplicitParam：一个请求参数
	// @ApiImplicitParams：多个请求参数

	@ApiOperation("根据StId获取SysUser")
	@ApiImplicitParam(name = "stId", value = "用户stId", required = true, dataType = "String")
	@GetMapping("/getSysUserByStId")
	@Override
	public SysUser getSysUserByStId(String stId) {
		SysUser sysUser = null;
		try {
			sysUser = new SysUser();
			sysUser.setStId(stId);
			sysUser.setStCode(port);
			// 设置业务处理时间为1.5S
			// Thread.sleep(1500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sysUser;
	}

	@ApiOperation("根据StId获取SysUser")
	@ApiImplicitParam(name = "sysUser", value = "用户sysUser", required = true, dataType = "SysUser")
	@PostMapping("/getSysUser")
	@Override
	public ResponseBase getSysUser(SysUser sysUser) {
		try {
			// 设置业务处理时间为1.5S
			Thread.sleep(1500);
			System.out.println(sysUser.getStCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseBase(200, "Order服务接口调用Base服务接口成功......", sysUser);
	}

}
