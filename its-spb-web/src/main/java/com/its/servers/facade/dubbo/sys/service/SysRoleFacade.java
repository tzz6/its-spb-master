package com.its.servers.facade.dubbo.sys.service;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.SysRole;


public interface SysRoleFacade {

	public int getSysRoleCount(Map<String, Object> map);

	public List<SysRole> getSysRoleList(Map<String, Object> map);

	public void insertSysRole(SysRole sysRole);

	public SysRole getSysRoleById(SysRole sysRole);

	public void updateSysRole(SysRole sysRole);

	public void deleteSysRole(List<String> roleIdList);

}
