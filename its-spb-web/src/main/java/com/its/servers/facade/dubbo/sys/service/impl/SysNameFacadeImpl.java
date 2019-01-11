package com.its.servers.facade.dubbo.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.model.mybatis.dao.domain.SysName;
import com.its.servers.facade.dubbo.sys.service.SysNameFacade;
import com.its.service.mybatis.SysNameService;


@Service("sysNameFacade")
public class SysNameFacadeImpl implements SysNameFacade {

	@Autowired
	private SysNameService sysNameService;

	@Override
	public List<SysName> getSysNameByLang(String lang) {
		return sysNameService.getSysNameByLang(lang);
	}

}
