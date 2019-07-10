package com.its.service.mybatis;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysName;

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
