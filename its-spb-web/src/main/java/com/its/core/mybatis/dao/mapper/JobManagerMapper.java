package com.its.core.mybatis.dao.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.its.model.mybatis.dao.domain.JobManager;


/**
 * @author tzz
 */
@Repository("jobManagerMapper")
public interface JobManagerMapper {



    /**
     * insert
     * @param jobManager
     */
	public void insertJobManager(JobManager jobManager);
	
	/**
	 * update
	 * @param jobManager
	 */
	public void updateJobManager(JobManager jobManager);

	/**
	 * get
	 * @param map
	 * @return
	 */
	public List<JobManager> getJobManagerList(Map<String, Object> map);

}
