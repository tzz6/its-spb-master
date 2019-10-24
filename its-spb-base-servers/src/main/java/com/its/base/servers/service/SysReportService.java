package com.its.base.servers.service;

import java.util.List;
import java.util.Map;
/**
 * 
 * description: SysReportService
 * company: 
 * @author: tzz
 * date: 2019/07/20 19:23
 */
public interface SysReportService {

    /**
     * 
     * description: execute
     * @author: 01115486
     * date: 2019/07/20 19:23
     * @param sql
     * @return List<Map<String,Object>>
     */
	public List<Map<String, Object>> execute(String sql);

	/**
	 * 
	 * description: execute
	 * @author: 01115486
	 * date: 2019/07/20 19:23
	 * @param sql
	 * @param start
	 * @param end
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> execute(String sql, String start, String end);

	/**
	 * 
	 * description: getCount
	 * @author: 01115486
	 * date: 2019/07/20 19:23
	 * @param sql
	 * @return long
	 */
	public long getCount(String sql);

	/**
	 * 
	 * description: executeIUD
	 * @author: 01115486
	 * date: 2019/07/20 19:23
	 * @param sql
	 * @return Object
	 */
	public Object executeIUD(String sql);

}
