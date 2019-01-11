package com.its.service.mybatis.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.common.utils.BaseException;
import com.its.core.mybatis.dao.mapper.JobManagerMapper;
import com.its.model.mybatis.dao.domain.JobManager;
import com.its.service.mybatis.JobManagerService;

@Service("jobManagerService")
public class JobManagerServiceImpl implements JobManagerService {

	private static final Log log = LogFactory.getLog(JobManagerServiceImpl.class);

	@Autowired
	private JobManagerMapper jobManagerMapper;

	@Override
	public void insertJobManager(JobManager jobManager) {
		try {
			jobManagerMapper.insertJobManager(jobManager);
		} catch (Exception e) {
			log.error("后台用新增服务错误", e);
			throw new BaseException("后台用新增服务错误", e);
		}

	}

	@Override
	public void updateJobManager(JobManager jobManager) {
		try {
			jobManagerMapper.updateJobManager(jobManager);
		} catch (Exception e) {
			log.error("后台用update服务错误", e);
			throw new BaseException("后台用update服务错误", e);
		}

	}

	@Override
	public List<JobManager> getJobManagerList(Map<String, Object> map) {
		try {
			return jobManagerMapper.getJobManagerList(map);
		} catch (Exception e) {
			log.error("后台查询服务错误", e);
			throw new BaseException("后台查询服务错误", e);
		}
	}

}
