package com.its.base.servers.service;

import java.util.List;

import com.its.base.servers.api.sys.domain.SysUserRole;

/**
 * @author tzz
 */
public interface SysUserRoleService {

    /**
     * getSysUserRoleBystId
     * @param stId
     * @return
     */
	List<SysUserRole> getSysUserRoleBystId(String stId);

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
	void deleteSysUserRoleByRoleId(String roleId);

}
