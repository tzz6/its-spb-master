package com.its.servers.facade.dubbo.sys.service;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysRoleMenu;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/10
 * @Introduce: SysRoleMenuFacade
 */
public interface SysRoleMenuFacade {

    /**
     * getSysRoleMenuByRoleId
     * @param roleId
     * @return
     */
	public List<SysRoleMenu> getSysRoleMenuByRoleId(String roleId);

	/**
	 * saveSysRoleMenu
	 * @param list
	 */
	public void saveSysRoleMenu(List<SysRoleMenu> list);

	/**
	 * deleteSysRoleMenuByRoleId
	 * @param roleId
	 */
	public void deleteSysRoleMenuByRoleId(String roleId);
}
