package com.its.core.mybatis.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.its.model.mybatis.dao.domain.SysUser;


/**
 * @author tzz
 */
@Repository("sysUserMapper")
public interface SysUserMapper {

    /**
     * getSysUserListByStCode
     * @param stCode stCode
     * @return List
     */
	List<SysUser> getSysUserListByStCode(@Param("stCode") String stCode);

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
