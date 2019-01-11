package com.its.core.mybatis.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.its.model.mybatis.dao.domain.SysUser;



@Repository("sysUserMapper")
public interface SysUserMapper {

	public List<SysUser> getSysUserListByStCode(@Param("stCode") String stCode);

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
