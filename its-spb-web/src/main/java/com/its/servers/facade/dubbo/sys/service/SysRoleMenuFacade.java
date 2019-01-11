package com.its.servers.facade.dubbo.sys.service;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysRoleMenu;


public interface SysRoleMenuFacade {

	public List<SysRoleMenu> getSysRoleMenuByRoleId(String roleId);

	public void saveSysRoleMenu(List<SysRoleMenu> list);

	public void deleteSysRoleMenuByRoleId(String roleId);
}
