package com.its.service.mybatis.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.common.utils.BaseException;
import com.its.core.mybatis.dao.mapper.SQLExecuteMapper;
import com.its.service.mybatis.SysReportService;

@Service("sysReportService")
public class SysReportServiceImpl implements SysReportService {

	private static final Log log = LogFactory.getLog(SysUserServiceImpl.class);

	@Autowired
	private SQLExecuteMapper sqlExecuteMapper;

	public List<Map<String, Object>> execute(String sql) {
		List<Map<String, Object>> list = null;
		try {
			list = sqlExecuteMapper.execute(sql);
		} catch (Exception e) {
			log.error("后台查询用户服务错误execute", e);
			throw new BaseException("后台查询用户服务错误execute", e);
		}
		return list;

	}

	public List<Map<String, Object>> execute(String sql, String start, String end) {
		List<Map<String, Object>> list = null;
		String sqlstr = "SELECT * FROM (" + sql + ") R LIMIT " + start + "," + end;
		try {
			list = sqlExecuteMapper.execute(sqlstr);
		} catch (Exception e) {
			log.error("后台查询用户服务错误", e);
			throw new BaseException("后台查询用户服务错误", e);
		}
		return list;
	}

	public long getCount(String sql) {
		String sqlstr = "SELECT count(*) FROM ( " + sql + " ) R";
		return sqlExecuteMapper.getCount(sqlstr);
	}

	public Object executeIUD(String sql) {
		return sqlExecuteMapper.executeIUD(sql);
	}

}

