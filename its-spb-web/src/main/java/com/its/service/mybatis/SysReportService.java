package com.its.service.mybatis;

import java.util.List;
import java.util.Map;

public interface SysReportService {

	public List<Map<String, Object>> execute(String sql);

	public List<Map<String, Object>> execute(String sql, String start, String end);

	public long getCount(String sql);

	public Object executeIUD(String sql);

}
