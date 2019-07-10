package com.its.service.mybatis;

import java.util.List;
import java.util.Map;

import com.its.model.mybatis.dao.domain.JobManager;

/**
 * 定时任务业务数据管理表
 * 
 * @author tzz
 */
public interface JobManagerService {

    /**
     * insertJobManager
     * @param jobManager
     */
	public void insertJobManager(JobManager jobManager);

	/**
	 * updateJobManager
	 * @param jobManager
	 */
	public void updateJobManager(JobManager jobManager);

	/**
	 * getJobManagerList
	 * @param map
	 * @return
	 */
	public List<JobManager> getJobManagerList(Map<String, Object> map);

}
