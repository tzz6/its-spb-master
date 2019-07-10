package com.its.servers.facade.dubbo.sys.service;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysUserRole;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/10
 * @Introduce: SysUserRoleFacade
 */
public interface SysUserRoleFacade {

    /**
     * getSysUserRoleBystId
     * @param stId
     * @return
     */
	public List<SysUserRole> getSysUserRoleBystId(String stId);

	/**
	 * saveSysUserRole
	 * @param list
	 */
	void saveSysUserRole(List<SysUserRole> list);

	/**
	 * deleteSysUserRoleByStId
	 * @param stId
	 */
	void deleteSysUserRoleByStId(String stId);

	/**
	 * deleteSysUserRoleByRoleId
	 * @param roleId
	 */
	public void deleteSysUserRoleByRoleId(String roleId);

}
