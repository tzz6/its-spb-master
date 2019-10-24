package com.its.base.servers.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.base.servers.api.sys.domain.SysName;
import com.its.base.servers.mapper.SysNameMapper;
import com.its.base.servers.service.SysNameService;
import com.its.common.utils.BaseException;

/**
 * @author tzz
 */
@Service("sysNameService")
public class SysNameServiceImpl implements SysNameService {

	private static final Log log = LogFactory.getLog(SysNameServiceImpl.class);

	@Autowired
	private SysNameMapper sysNameMapper;

	@Override
	public List<SysName> getSysNameByLang(String lang) {
		try {
			return sysNameMapper.getSysNameByLang(lang);
		} catch (Exception e) {
			log.error("后台查询系统名称服务错误", e);
			throw new BaseException("后台查询系统名称错误", e);
		}
	}

	

}
