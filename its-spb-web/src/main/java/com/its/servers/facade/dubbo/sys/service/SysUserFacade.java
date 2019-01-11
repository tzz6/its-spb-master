package com.its.servers.facade.dubbo.sys.service;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.SysUser;



public interface SysUserFacade {

	public List<SysUser> getSysUserListByStCode(String stCode);

	public void insertSysUser(SysUser sysUser);
	
	public void updateSysUser(SysUser sysUser);

	public int getSysUserCount(Map<String, Object> map);
	
	public List<SysUser> getSysUserList(Map<String, Object> map);

	public SysUser getSysUserByStId(SysUser sysUser);

	public void deleteSysUser(List<String> stIdList);
	
	public SysUser login(Map<String, Object> map);
	
	public void updateSysUserPassword(SysUser sysUser);
	
	public List<SysUser> getAllSysUserList();
}
