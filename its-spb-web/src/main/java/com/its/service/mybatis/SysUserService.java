package com.its.service.mybatis;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.SysUser;



public interface SysUserService {

	public List<SysUser> getSysUserListByStCode(String stCode);

	public void insertSysUser(SysUser sysUser);
	
	public void updateSysUser(SysUser sysUser);

	public int getSysUserCount(Map<String, Object> map);

	public List<SysUser> getSysUserList(Map<String, Object> map);

	public SysUser getSysUserByStId(SysUser sysUser);

	public void deleteSysUser(List<String> stIdList);

	public List<SysUser> getSysUserByMap(Map<String, Object> map);
	
	public void updateSysUserPassword(SysUser sysUser);

	public List<SysUser> getAllSysUserList();

}
