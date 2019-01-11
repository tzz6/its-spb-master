package com.its.core.mybatis.dao.mapper;

import com.its.model.mybatis.dao.domain.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysMenuMapper {

	public List<SysMenu> getSysMenus();

	public List<SysMenu> getSysMenuListFirst(@Param("lang") String lang);

	public List<SysMenu> getSysMenuListByParentMenuId(@Param("parentMenuId") String parentMenuId, @Param("lang") String lang);

	public int getSysMenuCount(Map<String, Object> map);

	public List<SysMenu> getSysMenuList(Map<String, Object> map);

	public List<SysMenu> getSysMenuListByUser(Map<String, Object> map);

	public List<SysMenu> getInterceptorUserMenus(Map<String, Object> map);
}
