package com.its.service.mybatis;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.SysMenu;


/**
 * @author tzz
 */
public interface SysMenuService {

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
	public List<SysMenu> getSysMenuListFirst(String lang);

	/**
	 * getSysMenuListByParentMenuId
	 * @param parentMenuId
	 * @param lang
	 * @return
	 */
	public List<SysMenu> getSysMenuListByParentMenuId(String parentMenuId, String lang);

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
