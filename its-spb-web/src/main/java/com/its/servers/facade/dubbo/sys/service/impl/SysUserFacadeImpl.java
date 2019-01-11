package com.its.servers.facade.dubbo.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.model.mybatis.dao.domain.SysUser;
import com.its.servers.facade.dubbo.sys.service.SysUserFacade;
import com.its.service.mybatis.SysUserService;


@Service("sysUserFacade")
public class SysUserFacadeImpl implements SysUserFacade {

	@Autowired
	private SysUserService sysUserService;

	public List<SysUser> getSysUserListByStCode(String stCode) {
		return sysUserService.getSysUserListByStCode(stCode);
	}

	@Override
	public void insertSysUser(SysUser sysUser) {
		sysUserService.insertSysUser(sysUser);
	}
	
	@Override
	public void updateSysUser(SysUser sysUser) {
		sysUserService.updateSysUser(sysUser);
	}

	@Override
	public int getSysUserCount(Map<String, Object> map) {
		return sysUserService.getSysUserCount(map);
	}

	@Override
	public List<SysUser> getSysUserList(Map<String, Object> map) {
		return sysUserService.getSysUserList(map);
	}

	@Override
	public SysUser getSysUserByStId(SysUser sysUser) {
		return sysUserService.getSysUserByStId(sysUser);
	}

	@Override
	public void deleteSysUser(List<String> stIdList) {
		sysUserService.deleteSysUser(stIdList);
	}

	@Override
	public SysUser login(Map<String, Object> map) {
		List<SysUser> list = sysUserService.getSysUserByMap(map);
		if(list != null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void updateSysUserPassword(SysUser sysUser) {
		sysUserService.updateSysUserPassword(sysUser);
	}

	@Override
	public List<SysUser> getAllSysUserList() {
		return sysUserService.getAllSysUserList();
	}
	
}
