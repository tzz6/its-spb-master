package com.its.servers.facade.dubbo.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.model.mybatis.dao.domain.SysMenu;
import com.its.servers.facade.dubbo.sys.service.SysMenuFacade;
import com.its.service.mybatis.SysMenuService;



@Service("sysMenuFacade")
public class SysMenuFacadeImpl implements SysMenuFacade {

	@Autowired
	private SysMenuService sysMenuService;

	@Override
	public List<SysMenu> getSysMenus() {
		return sysMenuService.getSysMenus();
	}

	@Override
	public List<SysMenu> getSysMenuListFirst(String lang) {
		return sysMenuService.getSysMenuListFirst(lang);
	}

	@Override
	public List<SysMenu> getSysMenuListByParentMenuId(String parentMenuId, String lang) {
		return sysMenuService.getSysMenuListByParentMenuId(parentMenuId ,lang);
	}

	@Override
	public int getSysMenuCount(Map<String, Object> map) {
		return sysMenuService.getSysMenuCount(map);
	}

	@Override
	public List<SysMenu> getSysMenuList(Map<String, Object> map) {
		return sysMenuService.getSysMenuList(map);
	}

	@Override
	public List<SysMenu> getSysMenuListByUser(Map<String, Object> map) {
		return sysMenuService.getSysMenuListByUser(map);
	}

	@Override
	public List<SysMenu> getInterceptorUserMenus(Map<String, Object> map) {
		return sysMenuService.getInterceptorUserMenus(map);
	}

}
