package com.its.service.mybatis.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.common.utils.BaseException;
import com.its.core.mybatis.dao.mapper.SysUserRoleMapper;
import com.its.model.mybatis.dao.domain.SysUserRole;
import com.its.service.mybatis.SysUserRoleService;

@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements SysUserRoleService {

	private static final Log log = LogFactory.getLog(SysUserRoleServiceImpl.class);

	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;

	@Override
	public List<SysUserRole> getSysUserRoleBystId(String stId) {
		try {
			return sysUserRoleMapper.getSysUserRoleBystId(stId);
		} catch (Exception e) {
			log.error("后台查询用户角色服务错误", e);
			throw new BaseException("后台查询用户角色服务错误", e);
		}
	}

	@Override
	public void saveSysUserRole(List<SysUserRole> list) {
		try {
			sysUserRoleMapper.saveSysUserRole(list);
		} catch (Exception e) {
			log.error("后台save用户角色服务错误", e);
			throw new BaseException("后台save用户角色服务错误", e);
		}
	}

	@Override
	public void deleteSysUserRoleByStId(String stId) {
		try {
			sysUserRoleMapper.deleteSysUserRoleByStId(stId);
		} catch (Exception e) {
			log.error("后台delete用户角色服务错误deleteSysUserRoleByStId", e);
			throw new BaseException("后台delete用户角色服务错误deleteSysUserRoleByStId", e);
		}
	}

	@Override
	public void deleteSysUserRoleByRoleId(String roleId) {
		try {
			sysUserRoleMapper.deleteSysUserRoleByRoleId(roleId);
		} catch (Exception e) {
			log.error("后台delete用户角色服务错误", e);
			throw new BaseException("后台delete用户角色服务错误", e);
		}

	}

}
