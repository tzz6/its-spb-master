package com.its.base.servers.service;

import java.util.List;
import java.util.Map;

import com.its.base.servers.api.sys.domain.SysUser;


/**
 * @author tzz
 */
public interface SysUserService {

    /**
     * getSysUserListByStCode
     * @param stCode stCode
     * @return List
     */
	List<SysUser> getSysUserListByStCode(String stCode);

	/**
	 * insertSysUser
	 * @param sysUser sysUser
	 */
	void insertSysUser(SysUser sysUser);

	/**
	 * updateSysUser
	 * @param sysUser sysUser
	 */
	void updateSysUser(SysUser sysUser);

	/**
	 * getSysUserCount
	 * @param map map
	 * @return int
	 */
	int getSysUserCount(Map<String, Object> map);

	/**
	 * getSysUserList
	 * @param map map
	 * @return List
	 */
	List<SysUser> getSysUserList(Map<String, Object> map);

	/**
	 * getSysUserByStId
	 * @param sysUser sysUser
	 * @return SysUser
	 */
	SysUser getSysUserByStId(SysUser sysUser);

	/**
	 * deleteSysUser
	 * @param stIdList stIdList
	 */
	void deleteSysUser(List<String> stIdList);

	/**
	 *
	 * description: login
	 * @author: tzz
	 * date: 2019/08/29 17:08
	 * @param map map
	 * @return SysUser
	 */
	SysUser login(Map<String, Object> map);

	/**
	 * getSysUserByMap
	 * @param map map
	 * @return List
	 */
	List<SysUser> getSysUserByMap(Map<String, Object> map);

	/**
	 * updateSysUserPassword
	 * @param sysUser sysUser
	 */
	void updateSysUserPassword(SysUser sysUser);

	/**
	 * getAllSysUserList
	 * @return List
	 */
	List<SysUser> getAllSysUserList();

}
