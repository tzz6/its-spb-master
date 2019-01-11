package com.its.servers.facade.dubbo.sys.service;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysUserRole;


public interface SysUserRoleFacade {

	public List<SysUserRole> getSysUserRoleBystId(String stId);

	void saveSysUserRole(List<SysUserRole> list);

	void deleteSysUserRoleByStId(String stId);

	public void deleteSysUserRoleByRoleId(String roleId);

}
