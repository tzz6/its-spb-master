package com.its.service.mybatis.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.common.utils.BaseException;
import com.its.core.mybatis.dao.mapper.SysRoleMenuMapper;
import com.its.model.mybatis.dao.domain.SysRoleMenu;
import com.its.service.mybatis.SysRoleMenuService;


@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

	private static final Log log = LogFactory.getLog(SysRoleMenuServiceImpl.class);

	@Autowired
	private SysRoleMenuMapper sysRoleMenuMapper;

	@Override
	public List<SysRoleMenu> getSysRoleMenuByRoleId(String roleId) {
		try {
			return sysRoleMenuMapper.getSysRoleMenuByRoleId(roleId);
		} catch (Exception e) {
			log.error("后台查询角色菜单服务错误", e);
			throw new BaseException("后台查询角色菜单服务错误", e);
		}
	}

	@Override
	public void saveSysRoleMenu(List<SysRoleMenu> list) {
		try {
			sysRoleMenuMapper.saveSysRoleMenu(list);
		} catch (Exception e) {
			log.error("后台保存角色菜单服务错误", e);
			throw new BaseException("后台保存角色菜单服务错误", e);
		}
	}

	@Override
	public void deleteSysRoleMenuByRoleId(String roleId) {
		try {
			sysRoleMenuMapper.deleteSysRoleMenuByRoleId(roleId);
		} catch (Exception e) {
			log.error("后台删除角色菜单服务错误", e);
			throw new BaseException("后台删除角色菜单服务错误", e);
		}
	}

}
