package com.its.service.mybatis;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.SysMenu;



public interface SysMenuService {

	public List<SysMenu> getSysMenus();

	public List<SysMenu> getSysMenuListFirst(String lang);

	public List<SysMenu> getSysMenuListByParentMenuId(String parentMenuId, String lang);

	public int getSysMenuCount(Map<String, Object> map);

	public List<SysMenu> getSysMenuList(Map<String, Object> map);

	public List<SysMenu> getSysMenuListByUser(Map<String, Object> map);

	public List<SysMenu> getInterceptorUserMenus(Map<String, Object> map);

}
