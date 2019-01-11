package com.its.service.mybatis;

import java.util.List;

import com.its.model.mybatis.dao.domain.SysName;


public interface SysNameService {

	public List<SysName> getSysNameByLang(String lang);

}
