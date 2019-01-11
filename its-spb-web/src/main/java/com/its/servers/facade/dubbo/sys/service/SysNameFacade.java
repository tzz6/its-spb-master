package com.its.servers.facade.dubbo.sys.service;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysName;


public interface SysNameFacade {

	public List<SysName> getSysNameByLang(String lang);

}
