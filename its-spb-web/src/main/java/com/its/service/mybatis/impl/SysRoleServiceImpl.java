package com.its.service.mybatis.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.common.utils.BaseException;
import com.its.core.mybatis.dao.mapper.SysRoleMapper;
import com.its.model.mybatis.dao.domain.SysRole;
import com.its.service.mybatis.SysRoleService;


@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

	private static final Log log = LogFactory.getLog(SysRoleServiceImpl.class);

	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Override
	public int getSysRoleCount(Map<String, Object> map) {
		try {
			return sysRoleMapper.getSysRoleCount(map);
		} catch (Exception e) {
			log.error("后台查询角色服务错误getSysRoleCount", e);
			throw new BaseException("后台查询角色服务错误getSysRoleCount", e);
		}
	}

	@Override
	public List<SysRole> getSysRoleList(Map<String, Object> map) {
		try {
			return sysRoleMapper.getSysRoleList(map);
		} catch (Exception e) {
			log.error("后台查询角色服务错误getSysRoleList", e);
			throw new BaseException("后台查询角色服务错误getSysRoleList", e);
		}
	}

	@Override
	public void insertSysRole(SysRole sysRole) {
		try {
			sysRoleMapper.insertSysRole(sysRole);
		} catch (Exception e) {
			log.error("后台新增角色服务错误", e);
			throw new BaseException("后台新增角色服务错误", e);
		}

	}

	@Override
	public SysRole getSysRoleById(SysRole sysRole) {
		try {
			return sysRoleMapper.getSysRoleById(sysRole);
		} catch (Exception e) {
			log.error("后台查询角色服务错误", e);
			throw new BaseException("后台查询角色服务错误", e);
		}
	}

	@Override
	public void updateSysRole(SysRole sysRole) {
		try {
			sysRoleMapper.updateSysRole(sysRole);
		} catch (Exception e) {
			log.error("后台更新角色服务错误", e);
			throw new BaseException("后台更新角色服务错误", e);
		}
		
	}

	@Override
	public void deleteSysRole(List<String> roleIdList) {
		try {
			sysRoleMapper.deleteSysRole(roleIdList);
		} catch (Exception e) {
			log.error("后台删除角色服务错误", e);
			throw new BaseException("后台删除角色服务错误", e);
		}
		
	}

}
