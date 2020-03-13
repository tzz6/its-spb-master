package com.its.base.servers.service;

import java.util.List;
import java.util.Map;

import com.its.base.servers.api.sys.domain.JobManager;

/**
 * 定时任务业务数据管理表
 * 
 * @author tzz
 */
public interface JobManagerService {

    /**
     * insertJobManager
     * @param jobManager jobManager
     */
	void insertJobManager(JobManager jobManager);

	/**
	 * updateJobManager
	 * @param jobManager jobManager
	 */
	void updateJobManager(JobManager jobManager);

	/**
	 * getJobManagerList
	 * @param map map
	 * @return List
	 */
	List<JobManager> getJobManagerList(Map<String, Object> map);

}
