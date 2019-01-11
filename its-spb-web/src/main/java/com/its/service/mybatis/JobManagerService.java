package com.its.service.mybatis;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.JobManager;

/**
 * 定时任务业务数据管理表
 * 
 * 
 */
public interface JobManagerService {

	public void insertJobManager(JobManager jobManager);

	public void updateJobManager(JobManager jobManager);

	public List<JobManager> getJobManagerList(Map<String, Object> map);

}
