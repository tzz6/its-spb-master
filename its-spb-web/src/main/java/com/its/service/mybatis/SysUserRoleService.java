package com.its.service.mybatis;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysUserRole;


public interface SysUserRoleService {

	List<SysUserRole> getSysUserRoleBystId(String stId);

	void saveSysUserRole(List<SysUserRole> list);

	void deleteSysUserRoleByStId(String stId);

	void deleteSysUserRoleByRoleId(String roleId);

}
