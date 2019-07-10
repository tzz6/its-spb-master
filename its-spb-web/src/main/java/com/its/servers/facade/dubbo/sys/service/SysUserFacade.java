package com.its.servers.facade.dubbo.sys.service;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.SysUser;


/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/10
 * @Introduce: SysUserFacade
 */
public interface SysUserFacade {

    /**
     * getSysUserListByStCode
     * @param stCode
     * @return
     */
	public List<SysUser> getSysUserListByStCode(String stCode);

	/**
	 * insertSysUser
	 * @param sysUser
	 */
	public void insertSysUser(SysUser sysUser);
	
	/**
	 * updateSysUser
	 * @param sysUser
	 */
	public void updateSysUser(SysUser sysUser);

	/**
	 * getSysUserCount
	 * @param map
	 * @return
	 */
	public int getSysUserCount(Map<String, Object> map);
	
	/**
	 * getSysUserList
	 * @param map
	 * @return
	 */
	public List<SysUser> getSysUserList(Map<String, Object> map);

	/**
	 * getSysUserByStId
	 * @param sysUser
	 * @return
	 */
	public SysUser getSysUserByStId(SysUser sysUser);

	/**
	 * deleteSysUser
	 * @param stIdList
	 */
	public void deleteSysUser(List<String> stIdList);
	
	/**
	 * login
	 * @param map
	 * @return
	 */
	public SysUser login(Map<String, Object> map);
	
	/**
	 * updateSysUserPassword
	 * @param sysUser
	 */
	public void updateSysUserPassword(SysUser sysUser);
	
	/**
	 * getAllSysUserList
	 * @return
	 */
	public List<SysUser> getAllSysUserList();
}
