package com.its.base.servers.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.its.base.servers.api.sys.domain.SysUser;


/**
 * @author tzz
 */
@Repository("sysUserMapper")
public interface SysUserMapper {

    /**
     * getSysUserListByStCode
     * @param stCode
     * @return
     */
	public List<SysUser> getSysUserListByStCode(@Param("stCode") String stCode);

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
	 * getSysUserByMap
	 * @param map
	 * @return
	 */
	public List<SysUser> getSysUserByMap(Map<String, Object> map);

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
