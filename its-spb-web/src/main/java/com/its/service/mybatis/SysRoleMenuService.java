package com.its.service.mybatis;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysRoleMenu;


public interface SysRoleMenuService {

	List<SysRoleMenu> getSysRoleMenuByRoleId(String roleId);

	void saveSysRoleMenu(List<SysRoleMenu> list);

	void deleteSysRoleMenuByRoleId(String roleId);
}
