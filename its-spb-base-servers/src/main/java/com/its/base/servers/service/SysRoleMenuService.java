package com.its.base.servers.service;

import java.util.List;

import com.its.base.servers.api.sys.domain.SysRoleMenu;

/**
 * @author tzz
 */
public interface SysRoleMenuService {

    /**
     * getSysRoleMenuByRoleId
     * @param roleId
     * @return
     */
	List<SysRoleMenu> getSysRoleMenuByRoleId(String roleId);

	/**
	 * saveSysRoleMenu
	 * @param list
	 */
	void saveSysRoleMenu(List<SysRoleMenu> list);

	/**
	 * deleteSysRoleMenuByRoleId
	 * @param roleId
	 */
	void deleteSysRoleMenuByRoleId(String roleId);
}
