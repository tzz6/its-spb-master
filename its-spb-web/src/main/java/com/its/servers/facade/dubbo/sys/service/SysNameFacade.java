package com.its.servers.facade.dubbo.sys.service;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysName;

/**
 * 
 * @author tzz
 */
public interface SysNameFacade {

    /**
     * getSysNameByLang
     * @param lang
     * @return
     */
	public List<SysName> getSysNameByLang(String lang);

}
