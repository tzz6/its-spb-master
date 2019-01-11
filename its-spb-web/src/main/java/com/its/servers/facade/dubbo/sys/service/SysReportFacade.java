package com.its.servers.facade.dubbo.sys.service;

import java.util.List;
import java.util.Map;

public interface SysReportFacade {

	public List<Map<String, Object>> execute(String sql);

	public List<Map<String, Object>> execute(String sql, String start, String end);

	public long getCount(String sql);

	public Object executeIUD(String sql);

}
