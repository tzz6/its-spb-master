package com.its.servers.facade.dubbo.sys.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/10
 * @Introduce: SysReportFacade
 */
public interface SysReportFacade {

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
