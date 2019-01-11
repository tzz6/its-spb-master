package com.its.servers.facade.dubbo.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.model.mybatis.dao.domain.SysRoleMenu;
import com.its.servers.facade.dubbo.sys.service.SysRoleMenuFacade;
import com.its.service.mybatis.SysRoleMenuService;


@Service("sysRoleMenuFacade")
public class SysRoleMenuFacadeImpl implements SysRoleMenuFacade {

	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@Override
	public List<SysRoleMenu> getSysRoleMenuByRoleId(String roleId) {
		return sysRoleMenuService.getSysRoleMenuByRoleId(roleId);
	}

	@Override
	public void saveSysRoleMenu(List<SysRoleMenu> list) {
		sysRoleMenuService.saveSysRoleMenu(list);
	}

	@Override
	public void deleteSysRoleMenuByRoleId(String roleId) {
		sysRoleMenuService.deleteSysRoleMenuByRoleId(roleId);
	}

}
