package com.its.core.mybatis.dao.mapper;

import java.util.List;
import java.util.Map;

public interface SQLExecuteMapper {

	public List<Map<String, Object>> execute(String sql);

	public Long getCount(String sql);

	Object executeIUD(String sql);
}
