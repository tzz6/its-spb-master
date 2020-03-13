package com.its.base.servers.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.its.base.servers.api.sys.domain.SysRole;

/**
 * @author tzz
 */
@Repository("sysRoleMapper")
public interface SysRoleMapper {

    /**
     * getSysRoleCount
     * @param map map
     * @return int
     */
	int getSysRoleCount(Map<String, Object> map);

	/**
	 * getSysRoleList
	 * @param map map
	 * @return List
	 */
	List<SysRole> getSysRoleList(Map<String, Object> map);

	/**
	 * insertSysRole
	 * @param sysRole sysRole
	 */
	void insertSysRole(SysRole sysRole);

	/**
	 * getSysRoleById
	 * @param sysRole sysRole
	 * @return SysRole
	 */
	SysRole getSysRoleById(SysRole sysRole);

	/**
	 * updateSysRole
	 * @param sysRole sysRole
	 */
	void updateSysRole(SysRole sysRole);

	/**
	 * deleteSysRole
	 * @param roleIdList roleIdList
	 */
	void deleteSysRole(List<String> roleIdList);

}
