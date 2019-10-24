package com.its.base.servers.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.its.base.servers.api.sys.domain.SysMenu;

/**
 * @author tzz
 */
public interface SysMenuMapper {

    /**
     * getSysMenus
     * @return
     */
	public List<SysMenu> getSysMenus();

	/**
	 * getSysMenuListFirst
	 * @param lang
	 * @return
	 */
	public List<SysMenu> getSysMenuListFirst(@Param("lang") String lang);

	/**
	 * getSysMenuListByParentMenuId
	 * @param parentMenuId
	 * @param lang
	 * @return
	 */
	public List<SysMenu> getSysMenuListByParentMenuId(@Param("parentMenuId") String parentMenuId, @Param("lang") String lang);

	/**
	 * getSysMenuCount
	 * @param map
	 * @return
	 */
	public int getSysMenuCount(Map<String, Object> map);

	/**
	 * getSysMenuList
	 * @param map
	 * @return
	 */
	public List<SysMenu> getSysMenuList(Map<String, Object> map);

	/**
	 * getSysMenuListByUser
	 * @param map
	 * @return
	 */
	public List<SysMenu> getSysMenuListByUser(Map<String, Object> map);

	/**
	 * getInterceptorUserMenus
	 * @param map
	 * @return
	 */
	public List<SysMenu> getInterceptorUserMenus(Map<String, Object> map);
}
