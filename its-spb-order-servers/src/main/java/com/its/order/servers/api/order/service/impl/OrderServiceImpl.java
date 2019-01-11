package com.its.order.servers.api.order.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.its.base.servers.api.sys.domain.SysUser;
import com.its.common.model.ResponseBase;
import com.its.common.utils.DateUtil;
import com.its.order.servers.api.feign.SysRoleServiceFeign;
import com.its.order.servers.api.feign.SysUserServiceFeign;
import com.its.order.servers.api.order.service.OrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("SysUser服务接口")
@RestController
@RefreshScope // Spring Cloud Config 手动刷新
public class OrderServiceImpl implements OrderService {
	// 1.服务雪崩效应
	// 解释：默认情况下tomcat只有一个线程池去处理客户端发送的所有所有服务请求，这样的话在高并发情况下，如果客户端所有的请求堆积到
	// 同一个服务接口上，就会产生tomcat的所有线程池去处理该服务接口，可能会导致其他接口服务无法访问
	// 可何证明tomcat默认情况下只有一个线程池去处理所有客户端发送的请求，直接看线程池名称
	// 解决服务雪崩效应：使用服务隔离机制(线程池方式和信号量)，使用线程池方式实现隔离的原理：相当于每个接口(服务)都有自己独立的线程池，
	// 因为每个线程池互不影响，这样的话就可以解决服务雪崩效应

	// Hystrix服务保护框架，在微服务中Hystrix能够为我们解决那些事情
	// 1.断路器
	// 2.服务降级
	// 3.服务熔断
	// 4.服务隔离机制
	// 5.服务雪崩效应 连环雪崩效应(如果比较严重会导致整个微服务无法访问)

	// 基于Hystrix解决服务雪崩效应原理：
	// 1.服务降级：在高并发情况下，防止用户一直等待（在tomcat中没有线程进行处理客户端请求的时候，不应该让用户一直在转圈等待），
	// 使用服务降级方式(返回一个友好的提示直接给客户端，不会去处理请求，调用fallback客户端本地方法)，目的是为了提高用户体验
	// 如果调用其他接口超时的时候（默认为1秒时间）,如果在1秒内没有及时响应，默认情况下业务逻辑是可以执行的，但是会直接执行服务降级方法
	// 如:秒杀系统--提示当前请求人数过多，请稍后再试
	// 2.服务熔断：(类似于家用电保险丝)目的是为了保护服务，在高并发的情况下，如果请求达到一定的极限（可自己设置阀值），如果流程超出了设置的阀值，
	// 自动开启保护服务功能，使用服务降级方式返回一个友好提示，熔断和服务降级一起使用。
	// 3.服务隔离：隔离机制(线程池方式和信号量),高并发下使用线程池方式
	// 线程池隔离：设置为线程池隔离，则接口(服务)都有自己独立的线程池，互不影响 ，缺点是CPU占用率非常高，(核心关键接口，不是每个接口都需要使用)

	// 使用SpringCloud Config读取配置文件
	@Value("${spring.datasource.username}")
	private String datasourceUsername;

	@Autowired
	private SysUserServiceFeign sysUserServiceFeign;
	@Autowired
	private SysRoleServiceFeign sysRoleServiceFeign;

	@ApiOperation("根据StId获取SysUser")
	@ApiImplicitParam(name = "stId", value = "用户stId", required = true, dataType = "String")
	@GetMapping("/getOrderToBaseGetSysUserByStId")
	@Override
	public String getOrderToBaseGetSysUserByStId(String stId) {
		SysUser sysUser = sysUserServiceFeign.getSysUserByStId(stId);
		String result = null;
		if (sysUser != null) {
			result = sysUser.getStId() + "--" + sysUser.getStCode();
		} else {
			result = "未找到对应的SysUser";
		}
		// 线程名称组合：线程池名称+线程ID
		result = "当前线程名称：" + Thread.currentThread().getName() + result;
		System.out.println(result);
		return result;
	}

	/***
	 * 未处理服务雪崩效应
	 */
	@ApiOperation("未处理服务雪崩效应getOrderToBaseGetSysUser")
	@GetMapping("/getOrderToBaseGetSysUser")
	@Override
	public ResponseBase getOrderToBaseGetSysUser() {
		// 线程名称组合：线程池名称+线程ID
		String result = "当前线程名称：" + Thread.currentThread().getName();
		System.out.println(result);
		SysUser sysUser = new SysUser();
		sysUser.setStId("101");
		sysUser.setStCode("Test001");
		sysUser.setCreateTm(new Date());
		return sysUserServiceFeign.getSysUser(sysUser);
	}

	/**
	 * 解决服务雪崩效应 Hystrix <br/>
	 * 有两种方式配置保护服务 通过注解和接口形式<br/>
	 * 方式一：通过@HystrixCommand注解实现<br/>
	 * 方式二：@FeignClient中的fallback类级别实现，需要在yml中配置开启Feign的Hystrix熔断器<br/>
	 * 当前方法使用方式一：通过注解实现<br/>
	 * fallbackMethod 方法的作用 ：服务降级执行<br/>
	 * Hystrix默认开启线程池隔离方式，服务降级，服务熔断<br/>
	 * 
	 * @return
	 */
	@ApiOperation("解决服务雪崩效应getOrderToBaseGetSysUserHystrix--方式一：通过@HystrixCommand注解实现")
	@HystrixCommand(fallbackMethod = "getOrderToBaseGetSysUserHystrixFallback")
	@GetMapping("/getOrderToBaseGetSysUserHystrix")
	@Override
	public ResponseBase getOrderToBaseGetSysUserHystrix() {
		// 线程名称组合：线程池名称+线程ID
		String result = "当前线程名称：" + Thread.currentThread().getName() + "--" + DateUtil.getDateyyyyMMddHHmmss();
		System.out.println(result);
		SysUser sysUser = new SysUser();
		sysUser.setStId("101");
		sysUser.setStCode("Test001");
		sysUser.setCreateTm(new Date());
		return sysUserServiceFeign.getSysUser(sysUser);
	}

	/** 服务降级方法 */
	public ResponseBase getOrderToBaseGetSysUserHystrixFallback() {
		String result = "服务降级，友好提示，服务器忙请稍后重试" + Thread.currentThread().getName() + "--"
				+ DateUtil.getDateyyyyMMddHHmmss();
		System.out.println(result);
		return new ResponseBase(200, result, null);
	}

	/**
	 * 解决服务雪崩效应 Hystrix <br/>
	 * 方式二：@FeignClient中的fallback类级别实现，需要在yml中配置开启Feign的Hystrix熔断器<br/>
	 * 
	 * @return
	 */
	@ApiOperation("解决服务雪崩效应 getOrderToBaseGetSysRoleHystrix方式二：@FeignClient中的fallback类级别实现")
	@ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String")
	@GetMapping("/getOrderToBaseGetSysRoleHystrix/{id}")
	@Override
	public ResponseBase getOrderToBaseGetSysRoleHystrix(@PathVariable("id") String id) {
		// 线程名称组合：线程池名称+线程ID
		String result = "当前线程名称：" + Thread.currentThread().getName() + "--" + DateUtil.getDateyyyyMMddHHmmss();
		System.out.println(result);
		String value = sysRoleServiceFeign.getSysRoleById(id);
		ResponseBase responseBase = sysRoleServiceFeign.getSysRole();
		responseBase.setData(value + "---" + datasourceUsername);
		return responseBase;
	}

}
