package com.its.base.servers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.its.base.servers.api.sys.domain.SysUserRole;


/**
 * @author tzz
 */
@Repository("sysUserRoleMapper")
public interface SysUserRoleMapper {

    /**
     * getSysUserRoleBystId
     * @param stId
     * @return
     */
	List<SysUserRole> getSysUserRoleBystId(@Param("stId") String stId);

	/**
	 * saveSysUserRole
	 * @param list
	 */
	void saveSysUserRole(List<SysUserRole> list);

	/**
	 * deleteSysUserRoleByStId
	 * @param stId
	 */
	void deleteSysUserRoleByStId(@Param("stId") String stId);

	/**
	 * deleteSysUserRoleByRoleId
	 * @param roleId
	 */
	void deleteSysUserRoleByRoleId(String roleId);
}
