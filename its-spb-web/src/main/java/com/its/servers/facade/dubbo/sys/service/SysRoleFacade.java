package com.its.servers.facade.dubbo.sys.service;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.SysRole;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/10
 * @Introduce: SysRoleFacade
 */
public interface SysRoleFacade {

    /**
     * getSysRoleCount
     * @param map
     * @return
     */
	public int getSysRoleCount(Map<String, Object> map);

	/**
	 * getSysRoleList
	 * @param map
	 * @return
	 */
	public List<SysRole> getSysRoleList(Map<String, Object> map);

	/**
	 * insertSysRole
	 * @param sysRole
	 */
	public void insertSysRole(SysRole sysRole);

	/**
	 * getSysRoleById
	 * @param sysRole
	 * @return
	 */
	public SysRole getSysRoleById(SysRole sysRole);

	/**
	 * updateSysRole
	 * @param sysRole
	 */
	public void updateSysRole(SysRole sysRole);

	/**
	 * deleteSysRole
	 * @param roleIdList
	 */
	public void deleteSysRole(List<String> roleIdList);

}
