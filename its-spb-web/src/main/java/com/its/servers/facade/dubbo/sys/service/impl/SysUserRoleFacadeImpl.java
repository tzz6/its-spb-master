package com.its.servers.facade.dubbo.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.model.mybatis.dao.domain.SysUserRole;
import com.its.servers.facade.dubbo.sys.service.SysUserRoleFacade;
import com.its.service.mybatis.SysUserRoleService;


@Service("sysUserRoleFacade")
public class SysUserRoleFacadeImpl implements SysUserRoleFacade {

	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Override
	public List<SysUserRole> getSysUserRoleBystId(String stId) {
		return sysUserRoleService.getSysUserRoleBystId(stId);
	}

	@Override
	public void saveSysUserRole(List<SysUserRole> list) {
		sysUserRoleService.saveSysUserRole(list);
	}

	@Override
	public void deleteSysUserRoleByStId(String stId) {
		sysUserRoleService.deleteSysUserRoleByStId(stId);
	}

	@Override
	public void deleteSysUserRoleByRoleId(String roleId) {
		sysUserRoleService.deleteSysUserRoleByRoleId(roleId);
	}

}
