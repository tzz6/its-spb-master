package com.its.order.servers.api.order.service;

import com.its.common.model.ResponseBase;

public interface OrderService {

	public String getOrderToBaseGetSysUserByStId(String stId);

	public ResponseBase getOrderToBaseGetSysUser();

	public ResponseBase getOrderToBaseGetSysUserHystrix();

	public ResponseBase getOrderToBaseGetSysRoleHystrix(String id);
}
