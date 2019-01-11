package com.its.servers.facade.dubbo.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.model.mybatis.dao.domain.SysRole;
import com.its.servers.facade.dubbo.sys.service.SysRoleFacade;
import com.its.service.mybatis.SysRoleService;


@Service("sysRoleFacade")
public class SysRoleFacadeImpl implements SysRoleFacade {

	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public int getSysRoleCount(Map<String, Object> map) {
		return sysRoleService.getSysRoleCount(map);
	}

	@Override
	public List<SysRole> getSysRoleList(Map<String, Object> map) {
		return sysRoleService.getSysRoleList(map);
	}

	@Override
	public void insertSysRole(SysRole sysRole) {
		sysRoleService.insertSysRole(sysRole);
	}

	@Override
	public SysRole getSysRoleById(SysRole sysRole) {
		return sysRoleService.getSysRoleById(sysRole);
	}

	@Override
	public void updateSysRole(SysRole sysRole) {
		sysRoleService.updateSysRole(sysRole);
	}

	@Override
	public void deleteSysRole(List<String> roleIdList) {
		sysRoleService.deleteSysRole(roleIdList);
	}

}
