package com.its.base.servers.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.its.base.servers.api.sys.domain.SysRoleMenu;

/**
 * @author tzz
 */
@Repository("sysRoleMenuMapper")
public interface SysRoleMenuMapper {

    /**
     * getSysRoleMenuByRoleId
     * @param roleId roleId
     * @return List
     */
	List<SysRoleMenu> getSysRoleMenuByRoleId(@Param("roleId") String roleId);

	/**
	 * saveSysRoleMenu
	 * @param list list
	 */
	void saveSysRoleMenu(List<SysRoleMenu> list);

	/**
	 * deleteSysRoleMenuByRoleId
	 * @param roleId roleId
	 */
	void deleteSysRoleMenuByRoleId(@Param("roleId") String roleId);

}
