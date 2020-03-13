package com.its.base.servers.service;

import java.util.List;
import java.util.Map;

import com.its.base.servers.api.sys.domain.SysMenu;


/**
 * @author tzz
 */
public interface SysMenuService {

    /**
     * getSysMenus
     * @return List
     */
	List<SysMenu> getSysMenus();

	/**
	 * getSysMenuListFirst
	 * @param lang lang
	 * @return List
	 */
	List<SysMenu> getSysMenuListFirst(String lang);

	/**
	 * getSysMenuListByParentMenuId
	 * @param parentMenuId parentMenuId
	 * @param lang lang
	 * @return List
	 */
	List<SysMenu> getSysMenuListByParentMenuId(String parentMenuId, String lang);

	/**
	 * getSysMenuCount
	 * @param map map
	 * @return int
	 */
	int getSysMenuCount(Map<String, Object> map);

	/**
	 * getSysMenuList
	 * @param map map
	 * @return List
	 */
	List<SysMenu> getSysMenuList(Map<String, Object> map);

	/**
	 * getSysMenuListByUser
	 * @param map map
	 * @return List
	 */
	List<SysMenu> getSysMenuListByUser(Map<String, Object> map);

	/**
	 * getInterceptorUserMenus
	 * @param map map
	 * @return List
	 */
	List<SysMenu> getInterceptorUserMenus(Map<String, Object> map);

}
