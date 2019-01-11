package com.its.core.mybatis.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.its.model.mybatis.dao.domain.SysUserRole;


@Repository("sysUserRoleMapper")
public interface SysUserRoleMapper {

	List<SysUserRole> getSysUserRoleBystId(@Param("stId") String stId);

	void saveSysUserRole(List<SysUserRole> list);

	void deleteSysUserRoleByStId(@Param("stId") String stId);

	void deleteSysUserRoleByRoleId(String roleId);
}
