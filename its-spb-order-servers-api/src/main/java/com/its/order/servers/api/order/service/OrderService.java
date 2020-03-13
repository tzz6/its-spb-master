package com.its.order.servers.api.order.service;

import com.its.common.model.ResponseBase;
/**
 *
 * @author tzz
 * @date 2019/03/18
 * @Introduce: OrderService
 */
public interface OrderService {

    /**
     * getOrderToBaseGetSysUserByStId
     * @param stId stId
     * @return ResponseBase
     */
	String getOrderToBaseGetSysUserByStId(String stId);

	/**
	 * getOrderToBaseGetSysUser
	 * @return ResponseBase
	 */
	ResponseBase getOrderToBaseGetSysUser();

	/**
	 * getOrderToBaseGetSysUserHystrix
	 * @return ResponseBase
	 */
	ResponseBase getOrderToBaseGetSysUserHystrix();

	/**
	 * getOrderToBaseGetSysRoleHystrix
	 * @param id id
	 * @return ResponseBase
	 */
	ResponseBase getOrderToBaseGetSysRoleHystrix(String id);
}
