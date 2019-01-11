package com.its.core.mybatis.dao.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.its.model.mybatis.dao.domain.JobManager;



@Repository("jobManagerMapper")
public interface JobManagerMapper {


	public void insertJobManager(JobManager jobManager);
	
	public void updateJobManager(JobManager jobManager);

	public List<JobManager> getJobManagerList(Map<String, Object> map);

}
