package com.its.service.mybatis.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.common.utils.BaseException;
import com.its.core.mybatis.dao.mapper.SysMenuMapper;
import com.its.model.mybatis.dao.domain.SysMenu;
import com.its.service.mybatis.SysMenuService;


@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

	private static final Log log = LogFactory.getLog(SysMenuService.class);

	@Autowired
	private SysMenuMapper sysMenuMapper;

	public List<SysMenu> getSysMenus() {
		try {
			return sysMenuMapper.getSysMenus();
		} catch (Exception e) {
			log.error("后台查询SysMenu错误getSysMenus", e);
			throw new BaseException("后台查询SysMenu错误getSysMenus", e);
		}
	}

	@Override
	public List<SysMenu> getSysMenuListFirst(String lang) {
		try {
			return sysMenuMapper.getSysMenuListFirst(lang);
		} catch (Exception e) {
			log.error("后台查询SysMenu错误getSysMenuListFirst", e);
			throw new BaseException("后台查询SysMenu错误getSysMenuListFirst", e);
		}
	}

	@Override
	public List<SysMenu> getSysMenuListByParentMenuId(String parentMenuId, String lang) {
		try {
			return sysMenuMapper.getSysMenuListByParentMenuId(parentMenuId, lang);
		} catch (Exception e) {
			log.error("后台查询SysMenu错误ParentMenuId", e);
			throw new BaseException("后台查询SysMenu错误ParentMenuId", e);
		}
	}

	@Override
	public int getSysMenuCount(Map<String, Object> map) {
		try {
			return sysMenuMapper.getSysMenuCount(map);
		} catch (Exception e) {
			log.error("后台查询SysMenu错误getSysMenuCount", e);
			throw new BaseException("后台查询SysMenu错误getSysMenuCount", e);
		}
	}

	@Override
	public List<SysMenu> getSysMenuList(Map<String, Object> map) {
		try {
			return sysMenuMapper.getSysMenuList(map);
		} catch (Exception e) {
			log.error("后台查询SysMenu错误getSysMenuList", e);
			throw new BaseException("后台查询SysMenu错误getSysMenuList", e);
		}
	}

	@Override
	public List<SysMenu> getSysMenuListByUser(Map<String, Object> map) {
		try {
			return sysMenuMapper.getSysMenuListByUser(map);
		} catch (Exception e) {
			log.error("后台查询SysMenu错误getSysMenuListByUser", e);
			throw new BaseException("后台查询SysMenu错误getSysMenuListByUser", e);
		}
	}

	@Override
	public List<SysMenu> getInterceptorUserMenus(Map<String, Object> map) {
		try {
			return sysMenuMapper.getInterceptorUserMenus(map);
		} catch (Exception e) {
			log.error("后台查询SysMenu错误", e);
			throw new BaseException("后台查询SysMenu错误", e);
		}
	}

}
