package com.its.base.servers.service;

import java.util.List;

import com.its.base.servers.api.sys.domain.SysUserRole;

/**
 * @author tzz
 */
public interface SysUserRoleService {

    /**
     * getSysUserRoleBystId
     * @param stId stId
     * @return List
     */
	List<SysUserRole> getSysUserRoleBystId(String stId);

	/**
	 * saveSysUserRole
	 * @param list list
	 */
	void saveSysUserRole(List<SysUserRole> list);

	/**
	 * deleteSysUserRoleByStId
	 * @param stId stId
	 */
	void deleteSysUserRoleByStId(String stId);

	/**
	 * deleteSysUserRoleByRoleId
	 * @param roleId roleId
	 */
	void deleteSysUserRoleByRoleId(String roleId);

}
