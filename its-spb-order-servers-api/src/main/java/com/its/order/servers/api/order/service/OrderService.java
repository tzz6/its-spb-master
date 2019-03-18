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
     * @param stId
     * @return
     */
	public String getOrderToBaseGetSysUserByStId(String stId);

	/**
	 * getOrderToBaseGetSysUser
	 * @return
	 */
	public ResponseBase getOrderToBaseGetSysUser();

	/**
	 * getOrderToBaseGetSysUserHystrix
	 * @return
	 */
	public ResponseBase getOrderToBaseGetSysUserHystrix();

	/**
	 * getOrderToBaseGetSysRoleHystrix
	 * @param id
	 * @return
	 */
	public ResponseBase getOrderToBaseGetSysRoleHystrix(String id);
}
