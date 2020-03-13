package com.its.core.mybatis.dao.mapper;

import com.its.model.mybatis.dao.domain.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author tzz
 */
public interface SysMenuMapper {

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
	List<SysMenu> getSysMenuListFirst(@Param("lang") String lang);

	/**
	 * getSysMenuListByParentMenuId
	 * @param parentMenuId parentMenuId
	 * @param lang lang
	 * @return List
	 */
	List<SysMenu> getSysMenuListByParentMenuId(@Param("parentMenuId") String parentMenuId, @Param("lang") String lang);

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
