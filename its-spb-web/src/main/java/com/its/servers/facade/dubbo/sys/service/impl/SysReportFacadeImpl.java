package com.its.servers.facade.dubbo.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.servers.facade.dubbo.sys.service.SysReportFacade;
import com.its.service.mybatis.SysReportService;

@Service("sysReportFacade")
public class SysReportFacadeImpl implements SysReportFacade {
	@Autowired
	private SysReportService sysReportService;

	@Override
	public List<Map<String, Object>> execute(String sql) {
		return sysReportService.execute(sql);
	}

	@Override
	public List<Map<String, Object>> execute(String sql, String start, String end) {
		return sysReportService.execute(sql, start, end);
	}

	@Override
	public long getCount(String sql) {
		return sysReportService.getCount(sql);
	}

	@Override
	public Object executeIUD(String sql) {
		return sysReportService.executeIUD(sql);
	}

}
