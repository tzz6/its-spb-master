package com.its.base.servers.service;

import java.util.List;

import com.its.base.servers.api.sys.domain.SysName;

/**
 * @author tzz
 */
public interface SysNameService {

    /**
     * getSysNameByLang
     * @param lang
     * @return
     */
	public List<SysName> getSysNameByLang(String lang);

}
