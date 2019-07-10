package com.its.service.mybatis;

import java.util.List;
import java.util.Map;

/**
 * @author tzz
 */
public interface SysReportService {

    /**
     * execute
     * @param sql
     * @return
     */
	public List<Map<String, Object>> execute(String sql);

	/**
	 * execute
	 * @param sql
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> execute(String sql, String start, String end);

	/**
	 * getCount
	 * @param sql
	 * @return
	 */
	public long getCount(String sql);

	/**
	 * executeIUD
	 * @param sql
	 * @return
	 */
	public Object executeIUD(String sql);

}
