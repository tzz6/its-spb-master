package com.its.servers.facade.dubbo.sys.service;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.SysMenu;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/10
 * @Introduce: SysMenuFacade
 */
public interface SysMenuFacade {

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
